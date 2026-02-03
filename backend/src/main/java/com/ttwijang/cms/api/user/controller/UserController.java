package com.ttwijang.cms.api.user.controller;

import java.util.List;

import javax.validation.Valid;

import com.ttwijang.cms.api.attached_file.service.AttachedFileService;
import com.ttwijang.cms.api.user.dto.UserDto;
import com.ttwijang.cms.api.user.dto.mapper.UserMapper;
import com.ttwijang.cms.api.user.dto.search.UserSearch;
import com.ttwijang.cms.api.user.exception.UserNotFoundException;
import com.ttwijang.cms.api.user.service.UserService;
import com.ttwijang.cms.entity.AttachedFile;
import com.ttwijang.cms.entity.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

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

@Tag(name = "User", description = "사용자 관리 API")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AttachedFileService attachedFileService;

    @Operation(
            summary = "사용자 ID 중복 확인",
            description = "사용자 ID가 이미 존재하는지 확인합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "확인 성공",
                    content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "401", description = "인증 필요")
    })
    @GetMapping("/id-check/{userId}")
    public ResponseEntity<Boolean> userIdCheck(
            @Parameter(description = "확인할 사용자 ID", required = true, example = "user123")
            @PathVariable("userId") String userId) {
        return ResponseEntity.ok(userService.userIdCheck(userId));
    }

    @Operation(
            summary = "사용자 상세 조회",
            description = "UID로 특정 사용자의 상세 정보를 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = UserDto.Detail.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "401", description = "인증 필요")
    })
    @GetMapping("{uid}")
    public ResponseEntity<UserDto.Detail> detail(
            @Parameter(description = "사용자 UID", required = true)
            @PathVariable("uid") User user) {
        if (user == null)
            throw new UserNotFoundException("존재하지 않는 계정입니다.");
        return ResponseEntity.ok(UserMapper.INSTANCE.entityToDetailDto(user));
    }

    @Operation(
            summary = "사용자 목록 조회",
            description = "사용자 목록을 페이징하여 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
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