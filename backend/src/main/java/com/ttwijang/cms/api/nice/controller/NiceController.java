// package com.ttwijang.cms.api.nice.controller;

// import javax.servlet.http.HttpServletRequest;

// import com.ttwijang.cms.api.nice.service.NiceService;

// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;


// import lombok.AllArgsConstructor;

// @Controller
// @RequestMapping("/api/client/nice")
// @AllArgsConstructor
// public class NiceController {
	
// 	private final NiceService niceService;
	
// 	@GetMapping
// 	public ResponseEntity<String> getEncData(HttpServletRequest request) {
// 		return ResponseEntity.ok(niceService.getEncData(request));
// 	}
	
// 	@GetMapping("info")
// 	public String getInfo(HttpServletRequest request) {
// 		return "redirect:"+request.getScheme()+"://"+request.getServerName()+"/login-process?retInfo="+request.getParameter("retInfo");
// 	}

// 	@PostMapping("info")
// 	public String getInfoPost(HttpServletRequest request) {
// 		return "redirect:"+request.getScheme()+"://"+request.getServerName()+"/login-process?retInfo="+request.getParameter("retInfo");
// 	}
// }
