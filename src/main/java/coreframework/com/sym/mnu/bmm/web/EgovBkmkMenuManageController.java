package coreframework.com.sym.mnu.bmm.web;

import java.util.Map;

import javax.annotation.Resource;

import coreframework.com.cmm.util.CoreUserDetailsHelper;
import coreframework.com.sym.mnu.bmm.service.BkmkMenuManage;
import coreframework.com.sym.mnu.bmm.service.BkmkMenuManageVO;
import coreframework.com.sym.mnu.bmm.service.EgovBkmkMenuManageservice;
import coreframework.com.utl.fcc.service.EgovStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springmodules.validation.commons.DefaultBeanValidator;

import coreframework.com.cmm.LoginVO;
import coreframework.com.cmm.annotation.IncludedInfo;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
 * 바로가기메뉴관리 정보를 관리하기 위한 컨트롤러 클래스
 * @author 공통컴포넌트팀 윤성록
 * @since 2009.09.25
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.9.25  윤성록          최초 생성
 *   2011.8.26	정진오			IncludedInfo annotation 추가
 *
 * </pre>
 */

@Controller
public class EgovBkmkMenuManageController {

    @Resource(name = "bkmkMenuManageservice")
    private EgovBkmkMenuManageservice bkmkMenuManageService;

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    @Autowired
    private DefaultBeanValidator beanValidator;

    /**
     * 바로가기메뉴관리 정보에 대한 목록을 조회한다.
     *
     * @param BkmkMenuManageVO
     * @param status
     * @param model
     * @return
     * @throws Exception
     */
    @IncludedInfo(name="바로가기메뉴관리", order = 1110 ,gid = 60)
    @RequestMapping("/sym/mnu/bmm/selectBkmkMenuManageList.cm")
    public String selectBkmkMenuManageList(@ModelAttribute("searchVO") BkmkMenuManageVO bkmkMenuManageVO, SessionStatus status, ModelMap model) throws Exception {

        LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();

        Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();

        if(!isAuthenticated) {
            return "coreframework/com/uat/uia/CoreLoginUsr";
        }

        bkmkMenuManageVO.setPageUnit(propertyService.getInt("pageUnit"));
        bkmkMenuManageVO.setPageSize(propertyService.getInt("pageSize"));
        bkmkMenuManageVO.setUserId(user == null ? "" : EgovStringUtil.isNullToString(user.getId()));

        PaginationInfo paginationInfo = new PaginationInfo();

        paginationInfo.setCurrentPageNo(bkmkMenuManageVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(bkmkMenuManageVO.getPageUnit());
        paginationInfo.setPageSize(bkmkMenuManageVO.getPageSize());

        bkmkMenuManageVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        bkmkMenuManageVO.setLastIndex(paginationInfo.getLastRecordIndex());
        bkmkMenuManageVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

        bkmkMenuManageVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        bkmkMenuManageVO.setLastIndex(paginationInfo.getLastRecordIndex());
        bkmkMenuManageVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

        Map<String, Object> map = bkmkMenuManageService.selectBkmkMenuManageList(bkmkMenuManageVO);

        int totCnt = Integer.parseInt((String)map.get("resultCnt"));

        paginationInfo.setTotalRecordCount(totCnt);

        model.addAttribute("resultList", map.get("resultList"));
        model.addAttribute("resultCnt", map.get("resultCnt"));
        model.addAttribute("uniqId", user == null ? "" : EgovStringUtil.isNullToString(user.getUniqId()));
        model.addAttribute("paginationInfo", paginationInfo);

        return "coreframework/com/sym/mnu/bmm/EgovBkmkMenuManageList";

    }

    /**
     * 바로가기메뉴관리 정보를 삭제한다.
     *
     * @param checkMenuIds
     * @param bkmkMenuManageVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/sym/mnu/bmm/EgovBkmkMenuManageDelete.cm")
    public String deleteMenuManageList(
            @RequestParam("checkMenuIds") String checkMenuIds ,
            @ModelAttribute("bkmkMenuManageVO") BkmkMenuManageVO bkmkMenuManageVO,
            ModelMap model)
            throws Exception {

        LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();

        Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();

        if(!isAuthenticated) {
            //    model.addAttribute("message", egovMessageSource.getMessage("fail.common.login"));
            return "coreframework/com/uat/uia/CoreLoginUsr";
        }

        String [] temp = checkMenuIds.split(",");

        for(int i =0; i < temp.length; i++){
            BkmkMenuManage bkmk = new BkmkMenuManage();
            bkmk.setMenuId(temp[i]);
            bkmk.setUserId(user == null ? "" : EgovStringUtil.isNullToString(user.getId()));
            bkmkMenuManageService.deleteBkmkMenuManage(bkmk);
        }

        return "forward:/sym/mnu/bmm/selectBkmkMenuManageList.cm";
    }

    /**
     * 바로가기메뉴관리 등록화면으로 이동한다.
     *
     * @param BkmkMenuManage
     * @param status
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/sym/mnu/bmm/addBkmkInf.cm")
    public String addBkmkMenuManage( @ModelAttribute("bkmkMenuManage") BkmkMenuManage bkmkMenuManage, SessionStatus status, ModelMap model) throws Exception {

        if(!bkmkMenuManage.getMenuId().equals("")){

            bkmkMenuManage.setProgrmStrePath(bkmkMenuManageService.selectUrl(bkmkMenuManage));
        }

        return "coreframework/com/sym/mnu/bmm/EgovBkmkMenuManageRegist";
    }

    /**
     * 메뉴정보 목록팝업 화면으로 이동한다.
     *
     * @param commandMap
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/sym/mnu/bmm/openPopup.cm")
    public String openPopupWindow(@RequestParam Map<String, Object> commandMap, ModelMap model) throws Exception {

        String requestUrl = (String)commandMap.get("requestUrl");
        requestUrl = requestUrl.replaceAll("&", "&amp;");
        String width = (String)commandMap.get("width");
        String height = (String)commandMap.get("height");
        model.addAttribute("requestUrl", requestUrl + "?" + "&amp;PopFlag=Y");
        model.addAttribute("width", width);
        model.addAttribute("height", height);

        return "coreframework/com/sym/mnu/bmm/EgovModalPopupFrame";
    }

    /**
     * 메뉴정보 목록을 조회한다.
     *
     * @param BkmkMenuManageVO
     * @param commandMap
     * @param model
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unused")
	@RequestMapping("/sym/mnu/bmm/selectMenuList.cm")
    public String selectMenuList(@ModelAttribute("bkmkMenuManageVO") BkmkMenuManageVO bkmkMenuManageVO, @RequestParam Map<String, Object> commandMap, ModelMap model) throws Exception {
        String popFlag = (String)commandMap.get("PopFlag");

        LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();

        bkmkMenuManageVO.setPageUnit(propertyService.getInt("pageUnit"));
        bkmkMenuManageVO.setPageSize(propertyService.getInt("pageSize"));

        PaginationInfo paginationInfo = new PaginationInfo();

        paginationInfo.setCurrentPageNo(bkmkMenuManageVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(bkmkMenuManageVO.getPageUnit());
        paginationInfo.setPageSize(bkmkMenuManageVO.getPageSize());


        bkmkMenuManageVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        bkmkMenuManageVO.setLastIndex(paginationInfo.getLastRecordIndex());
        bkmkMenuManageVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

        bkmkMenuManageVO.setUserId(user == null ? "" : EgovStringUtil.isNullToString(user.getId()));

        Map<String, Object> map = bkmkMenuManageService.selectMenuList(bkmkMenuManageVO);

        int totCnt = Integer.parseInt((String)map.get("resultCnt"));
        paginationInfo.setTotalRecordCount(totCnt);

        model.addAttribute("resultList", map.get("resultList"));
        model.addAttribute("resultCnt", map.get("resultCnt"));
        model.addAttribute("paginationInfo", paginationInfo);

        return "coreframework/com/sym/mnu/bmm/EgovBkmkMenuPopup";
    }

    /**
     * 바로가기메뉴관리 정보를 등록한다.
     *
     * @param BkmkMenuManage
     * @param bindingResult
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/sym/mnu/bmm/registBkmkInf.cm")
    public String registBkmkInf(@ModelAttribute("bkmkMenuManage") BkmkMenuManage bkmkMenuManage,
            BindingResult bindingResult, SessionStatus status, ModelMap model) throws Exception {

        LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
        Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();

        if(!isAuthenticated) {
            return "coreframework/com/uat/uia/CoreLoginUsr";
        }

        beanValidator.validate(bkmkMenuManage, bindingResult);
        if (bindingResult.hasErrors()) {
            return "coreframework/com/sym/mnu/bmm/EgovBkmkMenuManageRegist";
        }

        bkmkMenuManage.setUserId(user == null ? "" : EgovStringUtil.isNullToString(user.getId()));
        if (isAuthenticated) {
            bkmkMenuManageService.insertBkmkMenuManage(bkmkMenuManage);
        }

        return "forward:/sym/mnu/bmm/selectBkmkMenuManageList.cm";
    }

    /**
     * 바로가기메뉴관리 미리보기 화면으로 이동한다.
     *
     * @param BkmkMenuManageVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/sym/mnu/bmm/previewBkmkInf.cm")
    public String previewBkmkInf(@ModelAttribute("searchVO") BkmkMenuManageVO bkmkMenuManageVO,ModelMap model)
            throws Exception {
        String resultMsg    = "";
        // 0. Spring Security 사용자권한 처리
        LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
        Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();

        if(!isAuthenticated) {
               return "coreframework/com/uat/uia/CoreLoginUsr";
        }

        bkmkMenuManageVO.setFirstIndex(0);
        bkmkMenuManageVO.setLastIndex(10);
        bkmkMenuManageVO.setRecordCountPerPage(10);

        bkmkMenuManageVO.setUserId(user == null ? "" : EgovStringUtil.isNullToString(user.getId()));

        Map<String, Object> map = bkmkMenuManageService.selectBkmkMenuManageList(bkmkMenuManageVO);

        model.addAttribute("list_menulist",  map.get("resultList"));
        model.addAttribute("resultMsg", resultMsg);

        return  "coreframework/com/sym/mnu/bmm/EgovBookMarkMenuPopup";
    }
}
