	package com.weilyeat.cms.api.attached_file.service;

	import java.io.IOException;
	import java.net.MalformedURLException;
	import java.nio.file.Files;
	import java.nio.file.Path;
	import java.nio.file.Paths;
	import java.nio.file.StandardCopyOption;
	import java.time.LocalDate;
	import java.time.format.DateTimeFormatter;
	import java.util.UUID;

	import javax.transaction.Transactional;

	import com.weilyeat.cms.api.attached_file.AttachedFileProperties;
	import com.weilyeat.cms.api.attached_file.exception.AttachedFileException;
	import com.weilyeat.cms.api.attached_file.exception.AttachedFileNotFoundException;
	import com.weilyeat.cms.api.attached_file.repository.AttachedFileRepository;
	import com.weilyeat.cms.entity.AttachedFile;

	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.core.io.Resource;
	import org.springframework.core.io.UrlResource;
	import org.springframework.stereotype.Service;
	import org.springframework.util.StringUtils;
	import org.springframework.web.multipart.MultipartFile;

	public interface AttachedFileService {

		AttachedFile save(MultipartFile file, String moduleName);

		void changeState(String fileUid, boolean useState);

		void changeState(String[] fileUidList, boolean useState);

		Resource getResource(AttachedFile attachedFile);
	}

	@Service
	class AttachedFileServiceImpl implements AttachedFileService {

		private final String uploadRoot;

		private final AttachedFileRepository attachedFileRepository;

		@Autowired
		public AttachedFileServiceImpl(AttachedFileProperties attachedFileProperties,
				AttachedFileRepository attachedFileRepository) {
			this.uploadRoot = attachedFileProperties.getUploadDirectory();
			this.attachedFileRepository = attachedFileRepository;
		}

		@Transactional
		@Override
		public AttachedFile save(MultipartFile file, String moduleName) {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());

			try {
				// 파일 이름 체크
				if (fileName.contains("..")) {
					throw new AttachedFileException("파일명에 부적합한 문자가 있습니다: " + fileName);
				}

				String fileUid = UUID.randomUUID().toString();
				String dateDirectory = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

				// 업로드 디렉토리가 없을 경우 생성
				Path targetDirectory = Paths.get(this.uploadRoot, moduleName, dateDirectory, fileUid);
				if (!Files.exists(targetDirectory)){
					Files.createDirectories(targetDirectory);
				}

				Files.copy(file.getInputStream(), targetDirectory, StandardCopyOption.REPLACE_EXISTING);

				StringBuilder filePathBuilder = new StringBuilder();
				filePathBuilder.append(moduleName).append("/").append(dateDirectory).append("/").append(fileUid);
				AttachedFile attachedFile = new AttachedFile(fileUid, file, filePathBuilder.toString());
				return attachedFileRepository.save(attachedFile);
			} catch (IOException ex) {
				ex.printStackTrace();
				throw new AttachedFileException("파일을 저장 하는데 실패했습니다: " + fileName, ex);
			}
		}

		@Transactional
		@Override
		public void changeState(String fileUid, boolean useState) {
			attachedFileRepository.findById(fileUid).ifPresent(file -> file.setUseYn(useState == true ? 'Y' : 'N'));
		}

		@Override
		public void changeState(String[] fileUidList, boolean useState) {
			for (String fileUid : fileUidList)
				changeState(fileUid, useState);
		}

		@Override
		public Resource getResource(AttachedFile attachedFile) {
			try {
				Path path = Paths.get(uploadRoot, attachedFile.getFilePath());
				Resource resource = new UrlResource(path.toUri());
				if (resource.exists() || resource.isReadable()) {
					return resource;
				} else {
					throw new AttachedFileNotFoundException("파일을 불러 올 수 없습니다.: " + attachedFile.getUid());
				}
			} catch (MalformedURLException e) {
				throw new AttachedFileNotFoundException("파일을 불러 올 수 없습니다.: " + attachedFile.getUid(), e);
			}
		}

	}
