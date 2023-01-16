package coreframework.com.cop.adb.web;

import java.util.Map;

import coreframework.com.cmm.util.CoreUserDetailsHelper;
import coreframework.com.utl.fcc.service.EgovStringUtil;
import coreframework.com.cmm.LoginVO;
import coreframework.com.cmm.annotation.IncludedInfo;
import coreframework.com.cop.adb.service.AddressBook;
import coreframework.com.cop.adb.service.AddressBookUser;
import coreframework.com.cop.adb.service.AddressBookUserVO;
import coreframework.com.cop.adb.service.AddressBookVO;
import coreframework.com.cop.adb.service.EgovAddressBookService;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springmodules.validation.commons.DefaultBeanValidator;

/**
 * 주소록정보를 관리하기 위한 컨트롤러 클래스
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
 *   2009.9.25  윤성록         최초 생성
 *   2011.8.26	정진오		  IncludedInfo annotation 추가
 *   2016.12.13 최두영         클래스명 변경
 * </pre>
 */

@Controller
public class EgovAddressBookController {

    @Resource(name = "EgovAdressBookService")
    private EgovAddressBookService adbkService;

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    @Autowired
    private DefaultBeanValidator beanValidator;

     /**
     * 주소록 정보에 대한 목록을 조회한다.
     *
     * @param adbkVO
     * @param status
     * @param model
     * @return
     * @throws Exception
     */
    @IncludedInfo(name="주소록관리", order = 380, gid = 40)
    @RequestMapping("/cop/adb/selectAdbkList.cm")
    public String selectAdressBookList(@ModelAttribute("searchVO") AddressBookVO adbkVO, ModelMap model) throws Exception {

        LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();

        Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();

        if(!isAuthenticated) {
            return "coreframework/com/uat/uia/CoreLoginUsr";
        }

        adbkVO.setPageUnit(propertyService.getInt("pageUnit"));
        adbkVO.setPageSize(propertyService.getInt("pageSize"));

        PaginationInfo paginationInfo = new PaginationInfo();

        paginationInfo.setCurrentPageNo(adbkVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(adbkVO.getPageUnit());
        paginationInfo.setPageSize(adbkVO.getPageSize());

        adbkVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        adbkVO.setLastIndex(paginationInfo.getLastRecordIndex());
        adbkVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        adbkVO.setWrterId(user == null ? "" : EgovStringUtil.isNullToString(user.getId()));
        adbkVO.setTrgetOrgnztId(user == null ? "" : EgovStringUtil.isNullToString(user.getOrgnztId()));

        Map<String, Object> map = adbkService.selectAdressBookList(adbkVO);
        int totCnt = Integer.parseInt((String)map.get("resultCnt"));


        paginationInfo.setTotalRecordCount(totCnt);

        model.addAttribute("resultList", map.get("resultList"));
        model.addAttribute("resultCnt", map.get("resultCnt"));
        model.addAttribute("userId", user == null ? "" : EgovStringUtil.isNullToString(user.getId()));
        model.addAttribute("paginationInfo", paginationInfo);

        return "coreframework/com/cop/adb/EgovAddressBookList";
    }

    /**
     * 주소록 정보에 대한 목록을 조회한다.(마이페이지 적용)
     *
     * @param adbkVO
     * @param status
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/adb/selectAdbkMainList.cm")
    public String selectAdressBookmainList(@ModelAttribute("searchVO") AddressBookVO adbkVO, ModelMap model) throws Exception {

        LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();

        Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();

        if(!isAuthenticated) {
            return "coreframework/com/uat/uia/CoreLoginUsr";
        }

        adbkVO.setPageUnit(propertyService.getInt("pageUnit"));
        adbkVO.setPageSize(propertyService.getInt("pageSize"));

        PaginationInfo paginationInfo = new PaginationInfo();

        paginationInfo.setCurrentPageNo(adbkVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(adbkVO.getPageUnit());
        paginationInfo.setPageSize(adbkVO.getPageSize());


        adbkVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        adbkVO.setLastIndex(paginationInfo.getLastRecordIndex());
        adbkVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        adbkVO.setWrterId(user == null ? "" : EgovStringUtil.isNullToString(user.getId()));
        adbkVO.setTrgetOrgnztId(user == null ? "" : EgovStringUtil.isNullToString(user.getOrgnztId()));

        Map<String, Object> map = adbkService.selectAdressBookList(adbkVO);
        int totCnt = Integer.parseInt((String)map.get("resultCnt"));
        paginationInfo.setTotalRecordCount(totCnt);

        model.addAttribute("resultList", map.get("resultList"));
        model.addAttribute("resultCnt", map.get("resultCnt"));
        model.addAttribute("paginationInfo", paginationInfo);

        return "coreframework/com/cop/adb/EgovAddressBookMainList";
    }

    /**
     * 주소록등록 화면으로 이동한다.
     *
     * @param adbkVO
     * @param status
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/adb/addAdbkInf.cm")
    public String addAdressBook(
    		@ModelAttribute("searchVO") AddressBookVO adbkVO, 
    		@ModelAttribute("adbk") AddressBookVO addressBookVO,
    		ModelMap model) throws Exception {
        return "coreframework/com/cop/adb/EgovAddressBookRegist";
    }

    /**
     * 주소록을 삭제한다.
     *
     * @param adbkVO
     * @param status
     * @param model
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unused")
	@RequestMapping("/cop/adb/deleteAdbkInf.cm")
    public String deleteAdressBook(@ModelAttribute("searchVO") AddressBookVO adbkVO, ModelMap model) throws Exception {

        LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();

        Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();

        AddressBook adbk = adbkService.selectAdressBook(adbkVO);
        adbk.setUseAt("N");
        adbk.setLastUpdusrId(user == null ? "" : EgovStringUtil.isNullToString(user.getId()));
        adbkService.deleteAdressBook(adbk);

        return "forward:/cop/adb/selectAdbkList.cm";
    }

    /**
     * 주소록의 구성원을 추가한다.
     *
     * @param userVO
     * @param adbkVO
     * @param checkCnd
     * @param status
     * @param model
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unused")
	@RequestMapping("/cop/adb/addUser.cm")
    public String addUser(@ModelAttribute("searchVO") AddressBookVO adbkVO, @ModelAttribute("adbkUserVO") AddressBookUserVO adbkUserVO,
            @RequestParam("checkCnd")String checkCnd, ModelMap model) throws Exception {

        LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
        Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();

        String[] tempId = EgovStringUtil.isNullToString(adbkUserVO.getUserId()).split(",");

        for(int i =0; i < tempId.length; i++){
            if(!tempId[i].equals("")){
                AddressBookUser adbkUser = adbkService.selectAdbkUser(tempId[i]);
                adbkVO.getAdbkMan().add(adbkUser);
            }
        }

        if(checkCnd.equals("regist"))
            return "coreframework/com/cop/adb/EgovAddressBookRegist";
        else{
            model.addAttribute("writer" , true);
            return "coreframework/com/cop/adb/EgovAddressBookUpdt";
        }
    }

    /**
     * 주소록의 구성원을 삭제한다.
     *
     * @param userVO
     * @param adbkVO
     * @param checkCnd
     * @param checkWord
     * @param status
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/adb/deleteUser.cm")
    public String deleteUser( @ModelAttribute("searchVO") AddressBookVO adbkVO, @ModelAttribute("adbkUserVO") AddressBookUserVO adbkUserVO,
            @RequestParam("checkWord")String checkWord, @RequestParam("checkCnd")String checkCnd, ModelMap model) throws Exception {

        @SuppressWarnings("unused")
		LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
        Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();

        if(!isAuthenticated) {
            return "coreframework/com/uat/uia/CoreLoginUsr";
        }

        String[] tempId = EgovStringUtil.isNullToString(adbkUserVO.getUserId()).split(",");

        String id = "";

        for(int i =0; i < tempId.length; i++){

            if(tempId[i].equals(checkWord)){
                continue;
            }

            if(!tempId[i].equals("")){
                AddressBookUser adbkUser = adbkService.selectAdbkUser(tempId[i]);
                adbkVO.getAdbkMan().add(adbkUser);
            }

            id += tempId[i] + ",";
        }

        adbkUserVO.setUserId(id);



        if(checkCnd.equals("regist"))
            return "coreframework/com/cop/adb/EgovAddressBookRegist";
        else{
            model.addAttribute("writer" , true);
            return "coreframework/com/cop/adb/EgovAddressBookUpdt";
        }
    }


    /**
     * 주소록 구성원 찾기 팝업화면으로 이동한다.
     *
     * @param commandMap
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/adb/openPopup.cm")
    public String openPopupWindow(@RequestParam Map<String, Object> commandMap, ModelMap model) throws Exception {

        String requestUrl = (String)commandMap.get("requestUrl");
        String width = (String)commandMap.get("width");
        String height = (String)commandMap.get("height");

        model.addAttribute("requestUrl", requestUrl);
        model.addAttribute("width", width);
        model.addAttribute("height", height);

        return "coreframework/com/cop/adb/EgovModalPopupFrame";
  }


    /**
     * 주소록 등록가능한 구성원을 조회한다.
     *
     * @param adbkUserVO
     * @param commandMap
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/adb/selectManList.cm")
    public String selectUserList(@ModelAttribute("searchVO") AddressBookUserVO adbkUserVO, @RequestParam Map<String, Object> commandMap, ModelMap model) throws Exception {

        if(adbkUserVO.getSearchCnd() == null || adbkUserVO.getSearchCnd().equals("")){
            adbkUserVO.setSearchCnd("0");
        }

        adbkUserVO.setPageUnit(propertyService.getInt("pageUnit"));
        adbkUserVO.setPageSize(propertyService.getInt("pageSize"));

        PaginationInfo paginationInfo = new PaginationInfo();

        paginationInfo.setCurrentPageNo(adbkUserVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(adbkUserVO.getPageUnit());
        paginationInfo.setPageSize(adbkUserVO.getPageSize());

        adbkUserVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        adbkUserVO.setLastIndex(paginationInfo.getLastRecordIndex());
        adbkUserVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

        Map<String, Object> map = null;

        int totCnt = 0;
        if(adbkUserVO.getSearchCnd().equals("0")){
            map = adbkService.selectManList(adbkUserVO);
            //2017.03.03 	조성원 	시큐어코딩(ES)-부적절한 예외 처리[CWE-253, CWE-440, CWE-754]
            totCnt = Integer.parseInt(EgovStringUtil.nullConvertInt(map.get("resultCnt")));
            paginationInfo.setTotalRecordCount(totCnt);
        }else{
            map = adbkService.selectCardList(adbkUserVO);
            //2017.03.03 	조성원 	시큐어코딩(ES)-부적절한 예외 처리[CWE-253, CWE-440, CWE-754]
            totCnt = Integer.parseInt(EgovStringUtil.nullConvertInt(map.get("resultCnt")));
            paginationInfo.setTotalRecordCount(totCnt);
        }

        model.addAttribute("resultList", map.get("resultList"));
        model.addAttribute("resultCnt", map.get("resultCnt"));
        model.addAttribute("paginationInfo", paginationInfo);

        return "coreframework/com/cop/adb/EgovAddressBookPopup";
    }

    
    /**
     * 주소록상세조회수정 화면으로 이동한다.
     *
     * @param adbkUserVO
     * @param commandMap
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/adb/updateAdbkInf.cm")
    public String updateAdbkInf(@ModelAttribute("searchVO") AddressBookVO adbkVO, ModelMap model) throws Exception {
    	
        LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
        Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();

        if(!isAuthenticated) {
            return "coreframework/com/uat/uia/CoreLoginUsr";
        }

        AddressBookVO tempAdbkVO = adbkService.selectAdressBook(adbkVO);

        AddressBookUserVO adbkUserVO = new AddressBookUserVO();

        boolean writer = false;
        String id = "";

        for(int i = 0; i < tempAdbkVO.getAdbkMan().size(); i++){
            if( tempAdbkVO.getAdbkMan().get(i).getNcrdId() == null){
                tempAdbkVO.getAdbkMan().get(i).setNcrdId("");
            } else {
            	tempAdbkVO.getAdbkMan().get(i).setNcrdId(tempAdbkVO.getAdbkMan().get(i).getNcrdId().trim());
            }
            if( tempAdbkVO.getAdbkMan().get(i).getEmplyrId() == null){
                tempAdbkVO.getAdbkMan().get(i).setEmplyrId("");
            }
        }
        for(int i = 0; i < tempAdbkVO.getAdbkMan().size(); i++){

            if(tempAdbkVO.getAdbkMan().get(i).getEmplyrId().equals(""))
                    {
                id += tempAdbkVO.getAdbkMan().get(i).getNcrdId() + ",";
            }else{
                id += tempAdbkVO.getAdbkMan().get(i).getEmplyrId() + ",";
            }
        }

        adbkUserVO.setUserId(id);

        if(tempAdbkVO.getWrterId().equals(user == null ? "" : EgovStringUtil.isNullToString(user.getId()))){
            writer = true;
        }

        model.addAttribute("searchVO", tempAdbkVO);
        model.addAttribute("adbkUserVO", adbkUserVO);
        model.addAttribute("writer" , writer);
        return "coreframework/com/cop/adb/EgovAddressBookUpdt";
    }

    /**
     * 주소록 정보를 등록한다.
     *
     * @param adbkVO
     * @param adbkUserVO
     * @param status
     * @param bindingResult
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/adb/RegistAdbkInf.cm")
    public String registadbk(@ModelAttribute("searchVO") AddressBookVO adbkVO, @ModelAttribute("adbkUserVO") AddressBookUserVO adbkUserVO,
        BindingResult bindingResult, ModelMap model) throws Exception {

        LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
        Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();

        beanValidator.validate(adbkVO, bindingResult);
        if (bindingResult.hasErrors()) {
            return "coreframework/com/cop/adb/EgovAddressBookRegist";
        }

        if(!isAuthenticated) {
            return "coreframework/com/uat/uia/CoreLoginUsr";
        }

        adbkVO.setWrterId(user == null ? "" : EgovStringUtil.isNullToString(user.getId()));
        adbkVO.setFrstRegisterId(user == null ? "" : EgovStringUtil.isNullToString(user.getId()));
        adbkVO.setLastUpdusrId(user == null ? "" : EgovStringUtil.isNullToString(user.getId()));

        String[] tempId = EgovStringUtil.isNullToString(adbkUserVO.getUserId()).split(",");


        for(int i =0; i < tempId.length; i++){
            if(!tempId[i].equals("")){
                AddressBookUser adbkUser = adbkService.selectAdbkUser(tempId[i]);
                adbkVO.getAdbkMan().add(adbkUser);
            }
        }

        if (isAuthenticated) {
            adbkVO.setTrgetOrgnztId(user == null ? "" : EgovStringUtil.isNullToString(user.getOrgnztId()));
            adbkService.insertAdressBook(adbkVO);
        }

        return "forward:/cop/adb/selectAdbkList.cm";
    }

    /**
     * 주소록 정보를 수정한다.
     *
     * @param adbkVO
     * @param adbkUserVO
     * @param status
     * @param bindingResult
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/adb/UpdateAddressBook.cm")
    public String updateAdressBook(@ModelAttribute("searchVO") AddressBookVO adbkVO,  @ModelAttribute("adbkUserVO") AddressBookUserVO adbkUserVO,
        BindingResult bindingResult, ModelMap model) throws Exception {

        LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
        Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();

        if(!isAuthenticated) {
            return "coreframework/com/uat/uia/CoreLoginUsr";
        }

        beanValidator.validate(adbkVO, bindingResult);

        if (bindingResult.hasErrors()) {
            return "coreframework/com/cop/adb/EgovAddressBookUpdate";
        }

        String[] tempId = EgovStringUtil.isNullToString(adbkUserVO.getUserId()).split(",");

        for(int i =0; i < tempId.length; i++){
            if(!tempId[i].equals("")){
                AddressBookUser adbkUser = adbkService.selectAdbkUser(tempId[i]);
                adbkVO.getAdbkMan().add(adbkUser);
            }
        }

        adbkVO.setLastUpdusrId(user == null ? "" : EgovStringUtil.isNullToString(user.getId()));
        adbkVO.setUseAt("Y");
        adbkService.updateAdressBook(adbkVO);

        return "forward:/cop/adb/selectAdbkList.cm";
    }

}
