/**
 * 개요
 * - 사용자부재에 대한 controller 클래스를 정의한다.
 *
 * 상세내용
 * - 사용자부재에 대한 등록, 수정, 삭제, 조회, 반영확인 기능을 제공한다.
 * - 사용자부재의 조회기능은 목록조회, 상세조회로 구분된다.
 * @author 이문준
 * @version 1.0
 * @created 03-8-2009 오후 2:09:35
 *  <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.8.03  이문준          최초 생성
 *   2011.8.26	정진오			IncludedInfo annotation 추가
 *
 * </pre>
 */

package coreframework.com.uss.ion.uas.web;

import javax.annotation.Resource;

import coreframework.com.uss.ion.uas.service.EgovUserAbsnceService;
import coreframework.com.uss.ion.uas.service.UserAbsnceVO;
import coreframework.com.utl.fcc.service.EgovStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springmodules.validation.commons.DefaultBeanValidator;

import coreframework.com.cmm.CoreMessageSource;
import coreframework.com.cmm.LoginVO;
import coreframework.com.cmm.annotation.IncludedInfo;
import coreframework.com.cmm.util.CoreUserDetailsHelper;
import coreframework.com.uss.ion.uas.service.UserAbsnce;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;


@Controller
public class EgovUserAbsnceController {




	@Resource(name="coreMessageSource")
    CoreMessageSource coreMessageSource;

    @Resource(name = "egovUserAbsnceService")
    private EgovUserAbsnceService egovUserAbsnceService;

    @Autowired
	private DefaultBeanValidator beanValidator;

    /**
	 * 사용자부재 목록화면 이동
	 * @return String
	 * @exception Exception
	 */
    @IncludedInfo(name="사용자부재관리", order = 790 ,gid = 50)
    @RequestMapping("/uss/ion/uas/selectUserAbsnceListView.cm")
    public String selectUserAbsnceListView() throws Exception {

        return "coreframework/com/uss/ion/uas/EgovUserAbsnceList";
    }

	/**
	 * 사용자부재정보를 관리하기 위해 등록된 사용자부재 목록을 조회한다.
	 * @param userAbsnceVO - 사용자부재 VO
	 * @return String - 리턴 Url
	 */
    @RequestMapping("/uss/ion/uas/selectUserAbsnceList.cm")
	public String selectUserAbsnceList(@RequestParam("selAbsnceAt") String selAbsnceAt,
			                           @ModelAttribute("userAbsnceVO") UserAbsnceVO userAbsnceVO,
			                            ModelMap model) throws Exception {

    	/** paging */
    	PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(userAbsnceVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(userAbsnceVO.getPageUnit());
		paginationInfo.setPageSize(userAbsnceVO.getPageSize());

		userAbsnceVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		userAbsnceVO.setLastIndex(paginationInfo.getLastRecordIndex());
		userAbsnceVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		userAbsnceVO.setSelAbsnceAt(selAbsnceAt);
		userAbsnceVO.setUserAbsnceList(egovUserAbsnceService.selectUserAbsnceList(userAbsnceVO));

		model.addAttribute("userAbsnceList", userAbsnceVO.getUserAbsnceList());

        int totCnt = egovUserAbsnceService.selectUserAbsnceListTotCnt(userAbsnceVO);
		paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);

        model.addAttribute("message", coreMessageSource.getMessage("success.common.select"));

        return "coreframework/com/uss/ion/uas/EgovUserAbsnceList";
	}

	/**
	 * 등록된 사용자부재 상세정보를 조회한다.
	 * @param userAbsnceVO - 사용자부재 VO
	 * @return String - 리턴 Url
	 */
    @RequestMapping("/uss/ion/uas/getUserAbsnce.cm")
	public String selectUserAbsnce(@RequestParam("userId") String userId,
			                       @ModelAttribute("userAbsnceVO") UserAbsnceVO userAbsnceVO,
			                       ModelMap model) throws Exception {

		userAbsnceVO.setUserId(userId);
		model.addAttribute("userAbsnce", egovUserAbsnceService.selectUserAbsnce(userAbsnceVO));
		model.addAttribute("message", coreMessageSource.getMessage("success.common.select"));

		UserAbsnceVO vo = (UserAbsnceVO)model.get("userAbsnce");

		if(vo.getRegYn().equals("N"))
		    return "coreframework/com/uss/ion/uas/EgovUserAbsnceRegist";
		else
			return "coreframework/com/uss/ion/uas/EgovUserAbsnceUpdt";
	}

	/**
	 * 사용자부재정보를 신규로 등록한다.
	 * @param userAbsnce - 사용자부재 model
	 * @return String - 리턴 Url
	 */
    @RequestMapping("/uss/ion/uas/addViewUserAbsnce.cm")
	public String insertUserAbsnceView(@RequestParam("userId") String userId,
			                           @ModelAttribute("userAbsnceVO") UserAbsnceVO userAbsnceVO,
			                            ModelMap model) throws Exception {
    	userAbsnceVO.setUserId(userId);
    	model.addAttribute("userAbsnce", egovUserAbsnceService.selectUserAbsnce(userAbsnceVO));
    	model.addAttribute("message", coreMessageSource.getMessage("success.common.select"));

//		return "coreframework/com/uss/ion/uas/EgovUserAbsnceRegist";
		return "forward:/uss/ion/uas/selectUserAbsnceList.cm";

	}

	/**
	 * 사용자부재정보를 신규로 등록한다.
	 * @param userAbsnce - 사용자부재 model
	 * @return String - 리턴 Url
	 */
    @RequestMapping("/uss/ion/uas/addUserAbsnce.cm")
	public String insertUserAbsnce(@ModelAttribute("userAbsnce") UserAbsnce userAbsnce,
			                       @ModelAttribute("userAbsnceVO") UserAbsnceVO userAbsnceVO,
		                            BindingResult bindingResult,
			                        ModelMap model) throws Exception {

    	beanValidator.validate(userAbsnce, bindingResult); //validation 수행

    	if (bindingResult.hasErrors()) {
    		model.addAttribute("userAbsnceVO", userAbsnceVO);
			return "coreframework/com/uss/ion/msi/EgovMainImageRegist";
		} else {
	   	    LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
	   	    userAbsnce.setLastUpdusrId(user == null ? "" : EgovStringUtil.isNullToString(user.getId()));

	   	    model.addAttribute("userAbsnce", egovUserAbsnceService.insertUserAbsnce(userAbsnce, userAbsnceVO));
			model.addAttribute("message", coreMessageSource.getMessage("success.common.insert"));

//			return "coreframework/com/uss/ion/uas/EgovUserAbsnceUpdt";
			return "forward:/uss/ion/uas/selectUserAbsnceList.cm";
		}
	}

	/**
	 * 기 등록된 사용자부재정보를 수정한다.
	 * @param userAbsnce - 사용자부재 model
	 * @return String - 리턴 Url
	 */
    @RequestMapping("/uss/ion/uas/updtUserAbsnce.cm")
	public String updateUserAbsnce(@ModelAttribute("userAbsnce") UserAbsnce userAbsnce,
			                        BindingResult bindingResult,
			                        ModelMap model) throws Exception {

    	if (bindingResult.hasErrors()) {
    		model.addAttribute("userAbsnceVO", userAbsnce);
			return "coreframework/com/uss/ion/uas/EgovUserAbsnceUpdt";
		} else {

	    	LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
	   	    userAbsnce.setLastUpdusrId(user == null ? "" : EgovStringUtil.isNullToString(user.getId()));

	    	egovUserAbsnceService.updateUserAbsnce(userAbsnce);
//	    	return "forward:/uss/ion/uas/getUserAbsnce.cm";
			return "forward:/uss/ion/uas/selectUserAbsnceList.cm";
		}
	}

	/**
	 * 기 등록된 사용자부재정보를 삭제한다.
	 * @param userAbsnce - 사용자부재 model
	 * @return String - 리턴 Url
	 */
    @RequestMapping("/uss/ion/uas/removeUserAbsnce.cm")
	public String deleteUserAbsnce(@ModelAttribute("userAbsnce") UserAbsnce userAbsnce,
                                    ModelMap model) throws Exception {

		egovUserAbsnceService.deleteUserAbsnce(userAbsnce);
    	model.addAttribute("message", coreMessageSource.getMessage("success.common.delete"));
		return "forward:/uss/ion/uas/selectUserAbsnceList.cm";
	}

	/**
	 * 기 등록된 사용자부재정보를 삭제한다.
	 * @param userAbsnce - 사용자부재 model
	 * @return String - 리턴 Url
	 */
    @RequestMapping("/uss/ion/uas/removeUserAbsnceList.cm")
	public String deleteUserAbsnceList(@RequestParam("userIds") String userIds ,
			                           @ModelAttribute("userAbsnce") UserAbsnce userAbsnce,
			                           ModelMap model) throws Exception {

    	String [] strUserIds = userIds.split(";");

    	for(int i=0; i<strUserIds.length;i++) {
    		userAbsnce.setUserId(strUserIds[i]);
    		egovUserAbsnceService.deleteUserAbsnce(userAbsnce);
    	}

    	model.addAttribute("message", coreMessageSource.getMessage("success.common.delete"));
		return "forward:/uss/ion/uas/selectUserAbsnceList.cm";
	}

	/**
	 * MyPage에 사용자부재정보를 제공하기 위해 목록을 조회한다.
	 * @param userAbsnceVO - 사용자부재 VO
	 * @return String - 리턴 Url
	 */
    @RequestMapping("/uss/ion/uas/selectUserAbsnceMainList.cm")
	public String selectUserAbsnceMainList(@ModelAttribute("userAbsnceVO") UserAbsnceVO userAbsnceVO,
			                                ModelMap model) throws Exception {

    	/** paging */
    	PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(userAbsnceVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(5);
		paginationInfo.setPageSize(userAbsnceVO.getPageSize());

		userAbsnceVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		userAbsnceVO.setLastIndex(paginationInfo.getLastRecordIndex());
		userAbsnceVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		userAbsnceVO.setSelAbsnceAt("A");
		userAbsnceVO.setUserAbsnceList(egovUserAbsnceService.selectUserAbsnceList(userAbsnceVO));

		model.addAttribute("userAbsnceList", userAbsnceVO.getUserAbsnceList());

        return "coreframework/com/uss/ion/uas/EgovUserAbsnceMainList";
	}
}
