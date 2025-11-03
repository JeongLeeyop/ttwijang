package com.weilyeat.cms.api.user.controller;

import java.util.List;

import javax.validation.Valid;

import com.weilyeat.cms.api.attached_file.service.AttachedFileService;
import com.weilyeat.cms.api.user.dto.UserDto;
import com.weilyeat.cms.api.user.dto.mapper.UserMapper;
import com.weilyeat.cms.api.user.dto.search.UserSearch;
import com.weilyeat.cms.api.user.exception.UserNotFoundException;
import com.weilyeat.cms.api.user.service.UserService;
import com.weilyeat.cms.entity.AttachedFile;
import com.weilyeat.cms.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AttachedFileService attachedFileService;

    @GetMapping("/id-check/{userId}")
    public ResponseEntity<Boolean> userIdCheck(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(userService.userIdCheck(userId));
    }

    @GetMapping("{uid}")
    public ResponseEntity<UserDto.Detail> detail(@PathVariable("uid") User user) {
        if (user == null)
            throw new UserNotFoundException("존재하지 않는 계정입니다.");
        return ResponseEntity.ok(UserMapper.INSTANCE.entityToDetailDto(user));
    }

    @GetMapping
    public ResponseEntity<Page<UserDto.Page>> list(UserSearch userSearch,
            @PageableDefault(direction = Direction.DESC, sort = { "createDate" }) Pageable pageable) {
        return ResponseEntity.ok(userService.list(userSearch, pageable));
    }

    @GetMapping("manager/list")
    public ResponseEntity<Page<UserDto.manager>> managerList(UserSearch userSearch,
            @PageableDefault(direction = Direction.DESC, sort = { "createDate" }) Pageable pageable) {
        return ResponseEntity.ok(userService.getManagerList(userSearch, pageable));
    }

    @GetMapping("/role/{roleUid}/list")
    public ResponseEntity<List<UserDto.Detail>> list(@PathVariable("roleUid") String roleUid) {
        List<UserDto.Detail> userList = userService.listByRole(roleUid);
        return ResponseEntity.ok(userList);
    }

    @PostMapping
    public ResponseEntity add(@RequestBody @Valid UserDto.JoinAdmin userDto) {
        userService.add(userDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("manager")
    public ResponseEntity addManager(@RequestBody @Valid UserDto.addManager dto) {
        userService.addManager(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/upload")
    public ResponseEntity<AttachedFile> fileUpload(MultipartFile file) {
        return ResponseEntity.ok(attachedFileService.save(file, "user"));
    }

    @PutMapping("{uid}")
    public ResponseEntity update(@PathVariable("uid") User user, @RequestBody @Valid UserDto.Update userDto) {
        if (user == null) throw new UserNotFoundException("존재하지 않는 계정입니다.");
        userService.update(user, userDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{uid}/enable")
    public ResponseEntity setEnable(@PathVariable("uid") String userUid, boolean enabled) {
        userService.setEnabled(userUid, enabled);
        return ResponseEntity.ok().build();
    }

    @PutMapping("manager/{uid}")
    public ResponseEntity updateManager(@PathVariable("uid") User user, @RequestBody @Valid UserDto.updateManager dto) {
        if (user == null) throw new UserNotFoundException("존재하지 않는 계정입니다.");
        userService.updateManager(user, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{uid}")
    public ResponseEntity delete(@PathVariable("uid") User user) {
        if (user == null) throw new UserNotFoundException("존재하지 않는 계정입니다.");
        userService.delete(user);
        return ResponseEntity.ok().build();
    }

}