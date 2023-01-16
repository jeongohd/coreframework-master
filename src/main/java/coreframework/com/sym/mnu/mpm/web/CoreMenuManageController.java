package coreframework.com.sym.mnu.mpm.web;

import coreframework.com.cmm.ComDefaultVO;
import coreframework.com.cmm.CoreMessageSource;
import coreframework.com.cmm.annotation.IncludedInfo;
import coreframework.com.cmm.util.CoreUserDetailsHelper;
import coreframework.com.sym.mnu.mpm.service.CoreMenuManageService;
import coreframework.com.sym.mnu.mpm.vo.MenuManageVO;
import coreframework.com.sym.prm.service.CoreProgrmManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;
import org.springmodules.validation.commons.DefaultBeanValidator;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 메뉴목록 관리및 메뉴생성, 사이트맵 생성을 처리하는 비즈니스 구현 클래스
 * @author 개발환경 개발팀 이용
 * @since 2009.06.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *  수정일               수정자           수정내용
 *  ----------   --------   ---------------------------
 *  2009.03.20   이  용            최초 생성
 *  2011.07.01   서준식            메뉴정보 삭제시 참조되고 있는 하위 메뉴가 있는지 체크하는 로직 추가
 *  2011.07.27   서준식            deleteMenuManageList() 메서드에서 메뉴 멀티 삭제 버그 수정
 *  2011.08.26   정진오            IncludedInfo annotation 추가
 *  2011.10.07   이기하            보안취약점 수정(파일 업로드시 엑셀파일만 가능하도록 추가)
 *  2015.05.28   조정국            메뉴리스트관리 선택시 "정상적으로 조회되었습니다"라는 alert창이 제일 먼저 뜨는것 수정 : 출력메시지 주석처리
 *  2020.11.02   신용호            KISA 보안약점 조치 - 자원해제
 *  2021.02.16   신용호            WebUtils.getNativeRequest(request,MultipartHttpServletRequest.class);
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class CoreMenuManageController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoreMenuManageController.class);

	/* Validator */
	@Autowired
	private DefaultBeanValidator beanValidator;
	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** EgovMenuManageMapper */
	@Resource(name = "CoreMenuManageService")
	private CoreMenuManageService coreMenuManageService;

	/** EgovMenuManageMapper */
	@Resource(name = "CoreProgrmManageService")
	private CoreProgrmManageService coreProgrmManageService;

	/** EgovFileMngService */
	//	@Resource(name="EgovFileMngService")
	//	private EgovFileMngService fileMngService;

	/** EgovFileMngUtil */
	//	@Resource(name="EgovFileMngUtil")
	//	private EgovFileMngUtil fileUtil;

	//	@Resource(name = "excelZipService")
	//    private EgovExcelService excelZipService;

	/** CoreMessageSource */
	@Resource(name = "coreMessageSource")
    CoreMessageSource coreMessageSource;

	/**
	 * 메뉴정보목록을 상세화면 호출 및 상세조회한다.
	 * @param req_menuNo  String
	 * @return 출력페이지정보 "sym/mnu/mpm/CoreMenuDetailSelectUpdt"
	 * @exception Exception
	 */
	@RequestMapping(value = "/sym/mnu/mpm/CoreMenuManageListDetailSelect.cm")
	public String selectMenuManage(
		@RequestParam("req_menuNo") String req_menuNo,
		@ModelAttribute("searchVO") ComDefaultVO searchVO,
		ModelMap model)
		throws Exception {
		// 0. Spring Security 사용자권한 처리
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			model.addAttribute("message", coreMessageSource.getMessage("fail.common.login"));
			return "coreframework/com/uat/uia/CoreLoginUsr";
		}
		searchVO.setSearchKeyword(req_menuNo);

		MenuManageVO resultVO = coreMenuManageService.selectMenuManage(searchVO);
		model.addAttribute("menuManageVO", resultVO);

		return "coreframework/com/sym/mnu/mpm/CoreMenuDetailSelectUpdt";
	}

	/**
	 * 메뉴목록 리스트조회한다.
	 * @param searchVO ComDefaultVO
	 * @return 출력페이지정보 "sym/mnu/mpm/CoreMenuManage"
	 * @exception Exception
	 */
	@IncludedInfo(name = "메뉴관리리스트", order = 1091, gid = 60)
	@RequestMapping(value = "/sym/mnu/mpm/coreMenuManageList.cm")
	public String coreMenuManageList(
		@ModelAttribute("searchVO") ComDefaultVO searchVO,
		ModelMap model)
		throws Exception {
		// 0. Spring Security 사용자권한 처리
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			model.addAttribute("message", coreMessageSource.getMessage("fail.common.login"));
			return "coreframework/com/uat/uia/CoreLoginUsr";
		}
		// 내역 조회
		/** EgovPropertyService.sample */
		searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
		searchVO.setPageSize(propertiesService.getInt("pageSize"));

		/** pageing */
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
		paginationInfo.setPageSize(searchVO.getPageSize());

		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		List<?> list_menumanage = coreMenuManageService.selectMenuManageList(searchVO);
		model.addAttribute("list_menumanage", list_menumanage);

		int totCnt = coreMenuManageService.selectMenuManageListTotCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt);
		model.addAttribute("paginationInfo", paginationInfo);

		return "coreframework/com/sym/mnu/mpm/CoreMenuManage";
	}

	/**
	 * 메뉴목록 멀티 삭제한다.
	 * @param checkedMenuNoForDel  String
	 * @return 출력페이지정보 "forward:/sym/mnu/mpm/coreMenuManageList.cm"
	 * @exception Exception
	 */
	@RequestMapping("/sym/mnu/mpm/coreMenuManageRemoves.cm")
	public String coreMenuManageRemoves(
		@RequestParam("checkedMenuNoForDel") String checkedMenuNoForDel,
		@ModelAttribute("menuManageVO") MenuManageVO menuManageVO,
		ModelMap model)
		throws Exception {
		// 0. Spring Security 사용자권한 처리
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			model.addAttribute("message", coreMessageSource.getMessage("fail.common.login"));
			return "coreframework/com/uat/uia/CoreLoginUsr";
		}
		String sLocationUrl = null;
		String resultMsg = "";

		String[] delMenuNo = checkedMenuNoForDel.split(",");
		menuManageVO.setMenuNo(Integer.parseInt(delMenuNo[0]));

		if (coreMenuManageService.selectUpperMenuNoByPk(menuManageVO) != 0) {
			resultMsg = coreMessageSource.getMessage("fail.common.delete.upperMenuExist");
			sLocationUrl = "forward:/sym/mnu/mpm/coreMenuManageList.cm";
		} else if (delMenuNo == null || (delMenuNo.length == 0)) {
			resultMsg = coreMessageSource.getMessage("fail.common.delete");
			sLocationUrl = "forward:/sym/mnu/mpm/coreMenuManageList.cm";
		} else {
			coreMenuManageService.deleteMenuManageList(checkedMenuNoForDel);
			resultMsg = coreMessageSource.getMessage("success.common.delete");
			sLocationUrl = "forward:/sym/mnu/mpm/coreMenuManageList.cm";
		}
		model.addAttribute("resultMsg", resultMsg);
		return sLocationUrl;
	}

	/**
	 * 메뉴정보를 등록화면으로 이동 및 등록 한다.
	 * @param menuManageVO    MenuManageVO
	 * @param commandMap      Map
	 * @return 출력페이지정보 등록화면 호출시 "sym/mnu/mpm/CoreMenuRegist",
	 *         출력페이지정보 등록처리시 "forward:/sym/mnu/mpm/coreMenuManageList.cm"
	 * @exception Exception
	 */
	@RequestMapping(value = "/sym/mnu/mpm/coreMenuRegist.cm")
	public String coreMenuRegist(
		@RequestParam Map<?, ?> commandMap,
		@ModelAttribute("menuManageVO") MenuManageVO menuManageVO,
		BindingResult bindingResult,
		ModelMap model)
		throws Exception {
		String sLocationUrl = null;
		String resultMsg = "";
		// 0. Spring Security 사용자권한 처리
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			model.addAttribute("message", coreMessageSource.getMessage("fail.common.login"));
			return "coreframework/com/uat/uia/CoreLoginUsr";
		}
		String sCmd = commandMap.get("cmd") == null ? "" : (String)commandMap.get("cmd");
		if (sCmd.equals("insert")) {
			beanValidator.validate(menuManageVO, bindingResult);
			if (bindingResult.hasErrors()) {
				sLocationUrl = "coreframework/com/sym/mnu/mpm/CoreMenuRegist";
				return sLocationUrl;
			}
			if (coreMenuManageService.selectMenuNoByPk(menuManageVO) == 0) {
				ComDefaultVO searchVO = new ComDefaultVO();
				searchVO.setSearchKeyword(menuManageVO.getProgrmFileNm());
				if (coreProgrmManageService.selectProgrmNMTotCnt(searchVO) == 0) {
					resultMsg = coreMessageSource.getMessage("fail.common.insert");
					sLocationUrl = "coreframework/com/sym/mnu/mpm/CoreMenuRegist";
				} else {
					coreMenuManageService.insertMenuManage(menuManageVO);
					resultMsg = coreMessageSource.getMessage("success.common.insert");
					sLocationUrl = "forward:/sym/mnu/mpm/coreMenuManageList.cm";
				}
			} else {
				resultMsg = coreMessageSource.getMessage("common.isExist.msg");
				sLocationUrl = "coreframework/com/sym/mnu/mpm/CoreMenuRegist";
			}
			model.addAttribute("resultMsg", resultMsg);
		} else {
			sLocationUrl = "coreframework/com/sym/mnu/mpm/CoreMenuRegist";
		}
		return sLocationUrl;
	}

	/**
	 * 메뉴정보를 수정 한다.
	 * @param menuManageVO  MenuManageVO
	 * @return 출력페이지정보 "forward:/sym/mnu/mpm/coreMenuManageList.cm"
	 * @exception Exception
	 */
	@RequestMapping(value = "/sym/mnu/mpm/coreMenuManageUpdt.cm")
	public String coreMenuManageUpdt(
		@ModelAttribute("menuManageVO") MenuManageVO menuManageVO,
		BindingResult bindingResult,
		ModelMap model)
		throws Exception {
		String sLocationUrl = null;
		String resultMsg = "";
		// 0. Spring Security 사용자권한 처리
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			model.addAttribute("message", coreMessageSource.getMessage("fail.common.login"));
			return "coreframework/com/uat/uia/CoreLoginUsr";
		}
		beanValidator.validate(menuManageVO, bindingResult);
		if (bindingResult.hasErrors()) {
			sLocationUrl = "forward:/sym/mnu/mpm/CoreMenuManageListDetailSelect.cm";
			return sLocationUrl;
		}
		ComDefaultVO searchVO = new ComDefaultVO();
		searchVO.setSearchKeyword(menuManageVO.getProgrmFileNm());
		if (coreProgrmManageService.selectProgrmNMTotCnt(searchVO) == 0) {
			resultMsg = coreMessageSource.getMessage("fail.common.update");
			sLocationUrl = "forward:/sym/mnu/mpm/CoreMenuManageListDetailSelect.cm";
		} else {
			coreMenuManageService.updateMenuManage(menuManageVO);
			resultMsg = coreMessageSource.getMessage("success.common.update");
			sLocationUrl = "forward:/sym/mnu/mpm/coreMenuManageList.cm";
		}
		model.addAttribute("resultMsg", resultMsg);
		return sLocationUrl;
	}

	/**
	 * 메뉴정보를 삭제 한다.
	 * @param menuManageVO MenuManageVO
	 * @return 출력페이지정보 "forward:/sym/mnu/mpm/coreMenuManageList.cm"
	 * @exception Exception
	 */
	@RequestMapping(value = "/sym/mnu/mpm/coreMenuManageRemove.cm")
	public String coreMenuManageRemove(
		@ModelAttribute("menuManageVO") MenuManageVO menuManageVO,
		ModelMap model)
		throws Exception {
		String resultMsg = "";
		// 0. Spring Security 사용자권한 처리
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			model.addAttribute("message", coreMessageSource.getMessage("fail.common.login"));
			return "coreframework/com/uat/uia/CoreLoginUsr";
		}
		if (coreMenuManageService.selectUpperMenuNoByPk(menuManageVO) != 0) {
			resultMsg = coreMessageSource.getMessage("fail.common.delete.upperMenuExist");
			model.addAttribute("resultMsg", resultMsg);
			return "forward:/sym/mnu/mpm/coreMenuManageList.cm";
		}

		coreMenuManageService.deleteMenuManage(menuManageVO);
		resultMsg = coreMessageSource.getMessage("success.common.delete");
		String _MenuNm = "%";
		menuManageVO.setMenuNm(_MenuNm);
		model.addAttribute("resultMsg", resultMsg);
		return "forward:/sym/mnu/mpm/coreMenuManageList.cm";
	}

	/**
	 * 메뉴리스트를 조회한다.
	 * @param searchVO ComDefaultVO
	 * @return 출력페이지정보 "sym/mnu/mpm/CoreMenuList"
	 * @exception Exception
	 */
	@IncludedInfo(name = "메뉴리스트관리", order = 1090, gid = 60)
	@RequestMapping(value = "/sym/mnu/mpm/coreMenuList.cm")
	public String coreMenuList(@ModelAttribute("searchVO") ComDefaultVO searchVO,
		ModelMap model)
		throws Exception {
		String resultMsg = "";
		// 0. Spring Security 사용자권한 처리
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			model.addAttribute("message", coreMessageSource.getMessage("fail.common.login"));
			return "coreframework/com/uat/uia/CoreLoginUsr";
		}
		List<?> list_menulist = coreMenuManageService.selectMenuList();
		resultMsg = coreMessageSource.getMessage("success.common.select");
		model.addAttribute("list_menulist", list_menulist);
		//        model.addAttribute("resultMsg", resultMsg);
		return "coreframework/com/sym/mnu/mpm/CoreMenuList";
	}

	/**
	 * 메뉴리스트의 메뉴정보를 등록한다.
	 * @param menuManageVO MenuManageVO
	 * @return 출력페이지정보 "sym/mnu/mpm/CoreMenuList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/sym/mnu/mpm/coreMenuAdd.cm")
	public String coreMenuAdd(
		@ModelAttribute("menuManageVO") MenuManageVO menuManageVO,
		BindingResult bindingResult,
		ModelMap model)
		throws Exception {
		String sLocationUrl = null;
		String resultMsg = "";
		// 0. Spring Security 사용자권한 처리
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			model.addAttribute("message", coreMessageSource.getMessage("fail.common.login"));
			return "coreframework/com/uat/uia/CoreLoginUsr";
		}

		beanValidator.validate(menuManageVO, bindingResult);
		if (bindingResult.hasErrors()) {
			sLocationUrl = "coreframework/com/sym/mnu/mpm/CoreMenuList";
			return sLocationUrl;
		}

		if (coreMenuManageService.selectMenuNoByPk(menuManageVO) == 0) {
			ComDefaultVO searchVO = new ComDefaultVO();
			searchVO.setSearchKeyword(menuManageVO.getProgrmFileNm());
			if (coreProgrmManageService.selectProgrmNMTotCnt(searchVO) == 0) {
				resultMsg = coreMessageSource.getMessage("fail.common.insert");
				sLocationUrl = "forward:/sym/mnu/mpm/coreMenuList.cm";
			} else {
				coreMenuManageService.insertMenuManage(menuManageVO);
				resultMsg = coreMessageSource.getMessage("success.common.insert");
				sLocationUrl = "forward:/sym/mnu/mpm/coreMenuList.cm";
			}
		} else {
			resultMsg = coreMessageSource.getMessage("common.isExist.msg");
			sLocationUrl = "forward:/sym/mnu/mpm/coreMenuList.cm";
		}
		model.addAttribute("resultMsg", resultMsg);
		return sLocationUrl;
	}

	/**
	 * 메뉴리스트의 메뉴정보를 수정한다.
	 * @param menuManageVO MenuManageVO
	 * @return 출력페이지정보 "sym/mnu/mpm/CoreMenuList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/sym/mnu/mpm/coreMenuUpdt.cm")
	public String coreMenuUpdt(
		@ModelAttribute("menuManageVO") MenuManageVO menuManageVO,
		BindingResult bindingResult,
		ModelMap model)
		throws Exception {
		String sLocationUrl = null;
		String resultMsg = "";
		// 0. Spring Security 사용자권한 처리
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			model.addAttribute("message", coreMessageSource.getMessage("fail.common.login"));
			return "coreframework/com/uat/uia/CoreLoginUsr";
		}

		beanValidator.validate(menuManageVO, bindingResult);
		if (bindingResult.hasErrors()) {
			sLocationUrl = "forward:/sym/mnu/mpm/coreMenuList.cm";
			return sLocationUrl;
		}
		ComDefaultVO searchVO = new ComDefaultVO();
		searchVO.setSearchKeyword(menuManageVO.getProgrmFileNm());
		if (coreProgrmManageService.selectProgrmNMTotCnt(searchVO) == 0) {
			resultMsg = coreMessageSource.getMessage("fail.common.update");
			sLocationUrl = "forward:/sym/mnu/mpm/coreMenuList.cm";
		} else {
			coreMenuManageService.updateMenuManage(menuManageVO);
			resultMsg = coreMessageSource.getMessage("success.common.update");
			sLocationUrl = "forward:/sym/mnu/mpm/coreMenuList.cm";
		}
		model.addAttribute("resultMsg", resultMsg);
		return sLocationUrl;
	}

	/**
	 * 메뉴리스트의 메뉴정보를 삭제한다.
	 * @param menuManageVO MenuManageVO
	 * @return 출력페이지정보 "sym/mnu/mpm/CoreMenuList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/sym/mnu/mpm/coreMenuRemove.cm")
	public String coreMenuRemove(
		@ModelAttribute("menuManageVO") MenuManageVO menuManageVO,
		BindingResult bindingResult,
		ModelMap model)
		throws Exception {
		String sLocationUrl = null;
		String resultMsg = "";
		// 0. Spring Security 사용자권한 처리
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			model.addAttribute("message", coreMessageSource.getMessage("fail.common.login"));
			return "coreframework/com/uat/uia/CoreLoginUsr";
		}

		beanValidator.validate(menuManageVO, bindingResult);
		if (bindingResult.hasErrors()) {
			sLocationUrl = "coreframework/com/sym/mnu/mpm/CoreMenuList";
			return sLocationUrl;
		}
		coreMenuManageService.deleteMenuManage(menuManageVO);
		resultMsg = coreMessageSource.getMessage("success.common.delete");
		sLocationUrl = "forward:/sym/mnu/mpm/coreMenuList.cm";
		model.addAttribute("resultMsg", resultMsg);
		return sLocationUrl;
	}

	/**
	 * 메뉴리스트의 메뉴정보를 이동 메뉴목록을 조회한다.
	 * @param searchVO  ComDefaultVO
	 * @return 출력페이지정보 "sym/mnu/mpm/CoreMenuMvmn"
	 * @exception Exception
	 */
	@RequestMapping(value = "/sym/mnu/mpm/coreMenuMvmn.cm")
	public String coreMenuMvmn(
		@ModelAttribute("searchVO") ComDefaultVO searchVO,
		ModelMap model)
		throws Exception {
		// 0. Spring Security 사용자권한 처리
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			model.addAttribute("message", coreMessageSource.getMessage("fail.common.login"));
			return "coreframework/com/uat/uia/CoreLoginUsr";
		}

		List<?> list_menulist = coreMenuManageService.selectMenuList();
		model.addAttribute("list_menulist", list_menulist);
		return "coreframework/com/sym/mnu/mpm/CoreMenuMvmn";
	}

	/**
	 * 메뉴리스트의 메뉴정보를 이동 메뉴목록을 조회한다. (New)
	 * @param searchVO  ComDefaultVO
	 * @return 출력페이지정보 "sym/mnu/mpm/CoreMenuMvmn"
	 * @exception Exception
	 */
	@RequestMapping(value = "/sym/mnu/mpm/coreMenuMvmnNew.cm")
	public String coreMenuMvmnNew(
		@ModelAttribute("searchVO") ComDefaultVO searchVO,
		ModelMap model)
		throws Exception {
		// 0. Spring Security 사용자권한 처리
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			model.addAttribute("message", coreMessageSource.getMessage("fail.common.login"));
			return "coreframework/com/uat/uia/CoreLoginUsr";
		}

		List<?> list_menulist = coreMenuManageService.selectMenuList();
		model.addAttribute("list_menulist", list_menulist);
		return "coreframework/com/sym/mnu/mpm/CoreMenuMvmnNew";
	}

	/*### 일괄처리 프로세스 ###*/

	/**
	 * 메뉴생성 일괄삭제프로세스
	 * @param menuManageVO MenuManageVO
	 * @return 출력페이지정보 "sym/mnu/mpm/CoreMenuBndeRegist"
	 * @exception Exception
	 */
	@RequestMapping(value = "/sym/mnu/mpm/coreMenuBndeAllRemove.cm")
	public String coreMenuBndeAllRemove(
		@ModelAttribute("menuManageVO") MenuManageVO menuManageVO,
		ModelMap model)
		throws Exception {
		String resultMsg = "";
		// 0. Spring Security 사용자권한 처리
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			model.addAttribute("message", coreMessageSource.getMessage("fail.common.login"));
			return "coreframework/com/uat/uia/CoreLoginUsr";
		}
		coreMenuManageService.menuBndeAllDelete();
		resultMsg = coreMessageSource.getMessage("success.common.delete");
		model.addAttribute("resultMsg", resultMsg);
		return "coreframework/com/sym/mnu/mpm/CoreMenuBndeRegist";
	}

	/**
	 * 메뉴일괄등록화면 호출 및  메뉴일괄등록처리 프로세스
	 * @param commandMap    Map
	 * @param menuManageVO  MenuManageVO
	 * @param request       HttpServletRequest
	 * @return 출력페이지정보 "sym/mnu/mpm/CoreMenuBndeRegist"
	 * @exception Exception
	 */
	@RequestMapping(value = "/sym/mnu/mpm/coreMenuBndeRegist.cm")
	public String CoreMenuBndeRegist(
		@RequestParam Map<?, ?> commandMap,
		final HttpServletRequest request,
		@ModelAttribute("menuManageVO") MenuManageVO menuManageVO,
		ModelMap model)
		throws Exception {
		String sLocationUrl = null;
		String resultMsg = "";
		String sMessage = "";
		// 0. Spring Security 사용자권한 처리
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			model.addAttribute("message", coreMessageSource.getMessage("fail.common.login"));
			return "coreframework/com/uat/uia/CoreLoginUsr";
		}
		String sCmd = commandMap.get("cmd") == null ? "" : (String)commandMap.get("cmd");
		if (sCmd.equals("bndeInsert")) {
			//final MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			final MultipartHttpServletRequest multiRequest = WebUtils.getNativeRequest(request,
				MultipartHttpServletRequest.class);
			if (multiRequest != null) {//2022.01 Possible null pointer dereference due to return value of called method
				final Map<String, MultipartFile> files = multiRequest.getFileMap();
				Iterator<Entry<String, MultipartFile>> itr = files.entrySet().iterator();
				MultipartFile file;
				while (itr.hasNext()) {
					Entry<String, MultipartFile> entry = itr.next();
					file = entry.getValue();
					if (file.getOriginalFilename() != null && !"".equals(file.getOriginalFilename())) {
						// 2011.10.07 업로드 파일에 대한 확장자를 체크
						if (file.getOriginalFilename().endsWith(".xls")
							|| file.getOriginalFilename().endsWith(".xlsx")
							|| file.getOriginalFilename().endsWith(".XLS")
							|| file.getOriginalFilename().endsWith(".XLSX")) {

							if (coreMenuManageService.menuBndeAllDelete()) {
								// KISA 보안약점 조치 - 자원해제
								InputStream is = null;
								try {
									is = file.getInputStream();
									sMessage = coreMenuManageService.menuBndeRegist(menuManageVO, is);
								} catch (IOException e) {
									throw new IOException(e);
								} finally {
									if (is != null) {//2022.01.Possible null pointer dereference in method on exception path 처리
										is.close();
									}
								}
								resultMsg = sMessage;

							} else {
								resultMsg = coreMessageSource.getMessage("fail.common.msg");
								menuManageVO.setTmpCmd("EgovMenuBndeRegist Error!!");
								model.addAttribute("resultVO", menuManageVO);
							}
						} else {
							LOGGER.info("xls, xlsx 파일 타입만 등록이 가능합니다.");
							resultMsg = coreMessageSource.getMessage("fail.common.msg");
							model.addAttribute("resultMsg", resultMsg);
							return "coreframework/com/sym/mnu/mpm/CoreMenuBndeRegist";
						}
						// *********** 끝 ***********

					} else {
						resultMsg = coreMessageSource.getMessage("fail.common.msg");
					}
					file = null;
				}

			}
			sLocationUrl = "coreframework/com/sym/mnu/mpm/CoreMenuBndeRegist";
			model.addAttribute("resultMsg", resultMsg);
		} else {
			sLocationUrl = "coreframework/com/sym/mnu/mpm/CoreMenuBndeRegist";
		}
		return sLocationUrl;
	}
}