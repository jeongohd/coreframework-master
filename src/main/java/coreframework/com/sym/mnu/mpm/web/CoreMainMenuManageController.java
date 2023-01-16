package coreframework.com.sym.mnu.mpm.web;

import coreframework.com.cmm.LoginVO;
import coreframework.com.cmm.annotation.IncludedInfo;
import coreframework.com.cmm.util.CoreUserDetailsHelper;
import coreframework.com.sym.mnu.mpm.service.CoreMenuManageService;
import coreframework.com.sym.mnu.mpm.vo.MenuManageVO;
import coreframework.com.utl.fcc.service.EgovStringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

/**
 * 메인메뉴 해당링크 처리를 하는 비즈니스 구현 클래스
 * @author 개발환경 개발팀 이용
 * @since 2009.06.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.03.20  이  용          최초 생성
 *   2011.09.07  서준식          사용자 구분 오류 수정
 *   2015.06.19  조정국          미인증사용자에 대한 보안처리
 *   2018.10.12  이정은          메인페이지 통합(업무, 기업, 일반)
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class CoreMainMenuManageController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoreMainMenuManageController.class);

	/** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;

    /** EgovMenuManageMapper */
	@Resource(name = "CoreMenuManageService")
    private CoreMenuManageService coreMenuManageService;

    /** EgovFileMngService */
	//@Resource(name="EgovFileMngService")
	//private EgovFileMngService fileMngService;

    /** EgovFileMngUtil */
	//@Resource(name="EgovFileMngUtil")
	//private EgovFileMngUtil fileUtil;

    /*### 메인작업 ###*/
    /*Main Index 조회*/
    /**
     * Main메뉴의 Index를 조회한다.
     * @param menuNo  String
     * @param chkURL  String
     * @return 출력페이지정보 "menu_index"
     * @exception Exception
     */
    @RequestMapping(value="/sym/mnu/mpm/CoreMainMenuIndex.cm")
    public String selectMainMenuIndex(
    		@ModelAttribute("menuManageVO") MenuManageVO menuManageVO,
    		@RequestParam(value="menuNo", required=false) String menuNo,
    		@RequestParam(value="chkURL", required=false) String chkURL,
    		ModelMap model)
            throws Exception {

    	int iMenuNo = Integer.parseInt(menuNo);
    	menuManageVO.setMenuNo(iMenuNo);
    	//menuManageVO.setTempValue(chkURL);
        model.addAttribute("resultVO", menuManageVO);

        return "coreframework/com/menu_index";
    }

    /**
     * Head메뉴를 조회한다.
     * @param menuManageVO MenuManageVO
     * @return 출력페이지정보 "head"
     * @exception Exception
     */
    @RequestMapping(value="/sym/mnu/mpm/CoreMainMenu.cm")
    public String selectMainMenu(
    		@ModelAttribute("menuManageVO") MenuManageVO menuManageVO,
    		ModelMap model)
            throws Exception {

    	LoginVO user =
			(LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();

    	menuManageVO.setTmpId(user == null ? "" : EgovStringUtil.isNullToString(user.getId()));
    	menuManageVO.setTmpPassword(user == null ? "" : EgovStringUtil.isNullToString(user.getPassword()));
    	menuManageVO.setTmpUserSe(user == null ? "" : EgovStringUtil.isNullToString(user.getUserSe()));
    	menuManageVO.setTmpName(user == null ? "" : EgovStringUtil.isNullToString(user.getName()));
    	menuManageVO.setTmpEmail(user == null ? "" : EgovStringUtil.isNullToString(user.getEmail()));
    	menuManageVO.setTmpOrgnztId(user == null ? "" : EgovStringUtil.isNullToString(user.getOrgnztId()));
    	menuManageVO.setTmpUniqId(user == null ? "" : EgovStringUtil.isNullToString(user.getUniqId()));

    	List<?> list_headmenu = coreMenuManageService.selectMainMenuHead(menuManageVO);
        model.addAttribute("list_headmenu", list_headmenu);
		if (!(user == null ? "" : EgovStringUtil.isNullToString(user.getId())).equals("")) {
        	// 메인 페이지 이동
	    		return "coreframework/com/CoreMainView";
        } else {
        	// 오류 페이지 이동
        	return "coreframework/com/cmm/error/coreError";
        }
    }

    /**
     * Head메뉴를 조회한다.
     * @param menuManageVO MenuManageVO
     * @return 출력페이지정보  "main_head"
     * @exception Exception
     */
    @RequestMapping(value="/sym/mnu/mpm/CoreMainMenuHead.cm")
    public String selectMainMenuHead(
    		@ModelAttribute("menuManageVO") MenuManageVO menuManageVO,
    		ModelMap model)
            throws Exception {

    	LoginVO user =
			(LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();

    	menuManageVO.setTmpId(user == null ? "" : EgovStringUtil.isNullToString(user.getId()));
    	menuManageVO.setTmpPassword(user == null ? "" : EgovStringUtil.isNullToString(user.getPassword()));
    	menuManageVO.setTmpUserSe(user == null ? "" : EgovStringUtil.isNullToString(user.getUserSe()));
    	menuManageVO.setTmpName(user == null ? "" : EgovStringUtil.isNullToString(user.getName()));
    	menuManageVO.setTmpEmail(user == null ? "" : EgovStringUtil.isNullToString(user.getEmail()));
    	menuManageVO.setTmpOrgnztId(user == null ? "" : EgovStringUtil.isNullToString(user.getOrgnztId()));
    	menuManageVO.setTmpUniqId(user == null ? "" : EgovStringUtil.isNullToString(user.getUniqId()));

    	List<?> list_headmenu = coreMenuManageService.selectMainMenuHead(menuManageVO);
        model.addAttribute("list_headmenu", list_headmenu);
		if (!(user == null ? "" : EgovStringUtil.isNullToString(user.getUniqId())).equals("")) {
        	// 메인 페이지 이동
        		return "coreframework/com/main_head";
        } else {
        	// 오류 페이지 이동
        	return "coreframework/com/cmm/error/coreError";
        }
    }


    /**
     * 좌측메뉴를 조회한다.
     * @param menuManageVO MenuManageVO
     * @param vStartP      String
     * @return 출력페이지정보 "main_left"
     * @exception Exception
     */
    @RequestMapping(value="/sym/mnu/mpm/CoreMainMenuLeft.cm")
    public String selectMainMenuLeft(
    		@ModelAttribute("menuManageVO") MenuManageVO menuManageVO,
    		@RequestParam(value="vStartP", required=false) String vStartP,
    		ModelMap model)
            throws Exception {
    	int iMenuNo = Integer.parseInt(vStartP);
    	menuManageVO.setTempInt(iMenuNo);
        model.addAttribute("resultVO", menuManageVO);

    	LoginVO user =
			(LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();

    	menuManageVO.setTmpId(user == null ? "" : EgovStringUtil.isNullToString(user.getId()));
    	menuManageVO.setTmpPassword(user == null ? "" : EgovStringUtil.isNullToString(user.getPassword()));
    	menuManageVO.setTmpUserSe(user == null ? "" : EgovStringUtil.isNullToString(user.getUserSe()));
    	menuManageVO.setTmpName(user == null ? "" : EgovStringUtil.isNullToString(user.getName()));
    	menuManageVO.setTmpEmail(user == null ? "" : EgovStringUtil.isNullToString(user.getEmail()));
    	menuManageVO.setTmpOrgnztId(user == null ? "" : EgovStringUtil.isNullToString(user.getOrgnztId()));
    	menuManageVO.setTmpUniqId(user == null ? "" : EgovStringUtil.isNullToString(user.getUniqId()));

    	List<?> list_menulist = coreMenuManageService.selectMainMenuLeft(menuManageVO);
        model.addAttribute("list_menulist", list_menulist);
      	return "coreframework/com/main_left";
    }

    /**
     * 우측화면을 조회한다.
     * @param menuManageVO MenuManageVO
     * @param vStartP      String
     * @return 출력페이지정보 해당URL
     * @exception Exception
     */
    /*Right Menu 조회*/
    @RequestMapping(value="/sym/mnu/mpm/CoreMainMenuRight.cm")
    public String selectMainMenuRight(
    		@ModelAttribute("menuManageVO") MenuManageVO menuManageVO,
    		@RequestParam(value="vStartP", required=false) String vStartP,
    		ModelMap model)
            throws Exception {
    	int iMenuNo = Integer.parseInt(vStartP);
    	LoginVO user =
			(LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();

    	String forwardURL = null;
    	forwardURL = coreMenuManageService.selectLastMenuURL(iMenuNo, user == null ? "" : EgovStringUtil.isNullToString(user.getUniqId()));
      	return "forward:"+forwardURL;
    }

    /**
     * HOME 메인화면 조회한다.
     * @param menuManageVO  MenuManageVO
     * @return 출력페이지정보 "CoreMainView"
     * @exception Exception
     */
    @IncludedInfo(name="포털(예제) 메인화면", order = 1, gid = 0)
    @RequestMapping(value="/sym/mnu/mpm/CoreMainMenuHome.cm")
    public String selectMainMenuHome(
    		@ModelAttribute("menuManageVO") MenuManageVO menuManageVO,
    		ModelMap model)
            throws Exception {

    	LoginVO user =
			(LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();

		// 미인증 사용자에 대한 보안처리
    	if(user == null) {
			return "index";
    	}

    	menuManageVO.setTmpId(user.getId());
    	menuManageVO.setTmpPassword(user.getPassword());
    	menuManageVO.setTmpUserSe(user.getUserSe());
    	menuManageVO.setTmpName(user.getName());
    	menuManageVO.setTmpEmail(user.getEmail());
    	menuManageVO.setTmpOrgnztId(user.getOrgnztId());
    	menuManageVO.setTmpUniqId(user.getUniqId());

		List<?> list_headmenu = coreMenuManageService.selectMainMenuHead(menuManageVO);
		model.addAttribute("list_headmenu", list_headmenu);

		LOGGER.debug("## selectMainMenuHome ## getSUserSe 1: {}", user.getUserSe());
		LOGGER.debug("## selectMainMenuHome ## getSUserId 2: {}", user.getId());
		LOGGER.debug("## selectMainMenuHome ## getUniqId  2: {}", user.getUniqId());

		if (!user.getId().equals("")) {
        	// 메인 페이지 이동
			return "coreframework/com/CoreMainView";
        
        } else {
        	// 오류 페이지 이동
        	return "coreframework/com/cmm/error/coreError";
        }
    }
}