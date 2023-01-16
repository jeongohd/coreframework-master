/**
 * 개요
 * - 로그인정책에 대한 controller 클래스를 정의한다.
 * 
 * 상세내용
 * - 로그인정책에 대한 등록, 수정, 삭제, 조회, 반영확인 기능을 제공한다.
 * - 로그인정책의 조회기능은 목록조회, 상세조회로 구분된다.
 * @author lee.m.j
 * @version 1.0
 * @created 03-8-2009 오후 2:08:53
 * <pre>
 * == 개정이력(Modification Information) ==
 * 
 *   수정일       수정자           수정내용
 *  -------     --------    ---------------------------
 *  2009.8.3    이문준     최초 생성
 *  2011.8.26	정진오			IncludedInfo annotation 추가
 * </pre>
 */

package coreframework.com.uat.uap.web;

import coreframework.com.cmm.CoreMessageSource;
import coreframework.com.cmm.LoginVO;
import coreframework.com.cmm.annotation.IncludedInfo;
import coreframework.com.cmm.util.CoreUserDetailsHelper;
import coreframework.com.cmm.util.CoreUtil;
import coreframework.com.uat.uap.service.CoreLoginPolicyService;
import coreframework.com.uat.uap.vo.LoginPolicy;
import coreframework.com.uat.uap.vo.LoginPolicyVO;
import coreframework.com.utl.fcc.service.EgovStringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springmodules.validation.commons.DefaultBeanValidator;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CoreLoginPolicyController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoreLoginPolicyController.class);
    @Resource(name="coreMessageSource")
    CoreMessageSource coreMessageSource;
    
	@Resource(name="CoreLoginPolicyService")
	CoreLoginPolicyService coreLoginPolicyService;
	
    @Autowired
	private DefaultBeanValidator beanValidator;

	/**
	 * 로그인정책 목록 조회화면으로 이동한다.
	 * @return String - 리턴 Url
	 */
	@RequestMapping(value={"/uat/uap/coreLoginPolicyIdx.cm"})
	public String coreLoginPolicyIdx(HttpServletRequest request) throws Exception {
		//@PathVariable 사용

		return  "coreframework/com/uat/uap/CoreLoginPolicyIdx";
	}

	/**
	 * 로그인정책 목록을 조회한다.
	 * @param loginPolicyVO - 로그인정책 VO
	 * @return String - 리턴 Url
	 */
	@IncludedInfo(name="로그인정책관리", order = 30 ,gid = 10)
	@RequestMapping("/uat/uap/coreLoginPolicyList.cm")
	public String coreLoginPolicyList(@ModelAttribute("loginPolicyVO") LoginPolicyVO loginPolicyVO,
			                             ModelMap model) throws Exception {
		
    	/** paging */
    	PaginationInfo paginationInfo = new PaginationInfo();
	    paginationInfo.setCurrentPageNo(loginPolicyVO.getPageIndex());
	    paginationInfo.setRecordCountPerPage(loginPolicyVO.getPageUnit());
	    paginationInfo.setPageSize(loginPolicyVO.getPageSize());
		
	    loginPolicyVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
	    loginPolicyVO.setLastIndex(paginationInfo.getLastRecordIndex());
	    loginPolicyVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
	    loginPolicyVO.setLoginPolicyList(coreLoginPolicyService.selectLoginPolicyList(loginPolicyVO));
        model.addAttribute("loginPolicyList", loginPolicyVO.getLoginPolicyList());
        
        int totCnt = coreLoginPolicyService.selectLoginPolicyListTotCnt(loginPolicyVO);
	    paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);
        model.addAttribute("message", coreMessageSource.getMessage("success.common.select"));

		//json
		model.addAttribute("loginPolicyListJson", new CoreUtil().modelToJson(loginPolicyVO.getLoginPolicyList()));
		model.addAttribute("paginationInfoJson", new CoreUtil().modelToJson(paginationInfo));
		model.addAttribute("messageJson", new CoreUtil().modelToJson(coreMessageSource.getMessage("success.common.select")));

		return "coreframework/com/uat/uap/CoreLoginPolicyList";
	}

	/**
	 * 로그인정책 목록의 상세정보를 조회한다.
	 * @param loginPolicyVO - 로그인정책 VO
	 * @return String - 리턴 Url
	 */
	@RequestMapping("/uat/uap/coreLoginPolicy.cm")
	public String coreLoginPolicy(@RequestParam("emplyrId") String emplyrId,
			                        @ModelAttribute("loginPolicyVO") LoginPolicyVO loginPolicyVO, 
                                     ModelMap model) throws Exception {
		log.info("loginPolicyVO.getPageIndex()++"+loginPolicyVO.getPageIndex());
		log.info("loginPolicyVO.emplyrId() : "+loginPolicyVO.getEmplyrId());
		loginPolicyVO.setEmplyrId(emplyrId);

		model.addAttribute("loginPolicy", coreLoginPolicyService.selectLoginPolicy(loginPolicyVO));
		model.addAttribute("message", coreMessageSource.getMessage("success.common.select"));
		
		LoginPolicyVO vo = (LoginPolicyVO)model.get("loginPolicy");
		
		if(vo.getRegYn().equals("N")) 
			return "coreframework/com/uat/uap/CoreLoginPolicyRegist";
		else 
			return "coreframework/com/uat/uap/CoreLoginPolicyUpdt";
	}

	/**
	 * 로그인정책 정보 등록화면으로 이동한다.
	 * @param loginPolicy - 로그인정책 model
	 * @return String - 리턴 Url
	 */
	@RequestMapping("/uat/uap/coreLoginPolicyRegist.cm")
	public String coreLoginPolicyRegist(@RequestParam("emplyrId") String emplyrId,
                                        @ModelAttribute("loginPolicyVO") LoginPolicyVO loginPolicyVO, 
                                         ModelMap model) throws Exception {
		log.info("loginPolicyVO66666"+ loginPolicyVO.getPageIndex());
		loginPolicyVO.setEmplyrId(emplyrId);
		
		model.addAttribute("loginPolicy", coreLoginPolicyService.selectLoginPolicy(loginPolicyVO));
		model.addAttribute("message", coreMessageSource.getMessage("success.common.select"));
    	
		return "coreframework/com/uat/uap/CoreLoginPolicyRegist";
	}

	/**
	 * 로그인정책 정보를 신규로 등록한다.
	 * @param loginPolicy - 로그인정책 model
	 * @return String - 리턴 Url
	 */
	@RequestMapping("/uat/uap/coreLoginPolicyAdd.cm")
	public String coreLoginPolicyAdd(@ModelAttribute("loginPolicy") LoginPolicy loginPolicy,
			                         BindingResult bindingResult,
                                     ModelMap model) throws Exception {
		
		beanValidator.validate(loginPolicy, bindingResult); //validation 수행		
		
    	if (bindingResult.hasErrors()) { 
    		model.addAttribute("loginPolicyVO", loginPolicy);
			return "coreframework/com/uat/uap/CoreLoginPolicyRegist";
		} else {
			
			LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
			loginPolicy.setUserId(user == null ? "" : EgovStringUtil.isNullToString(user.getId()));

			coreLoginPolicyService.insertLoginPolicy(loginPolicy);
	    	model.addAttribute("message", coreMessageSource.getMessage("success.common.update"));
	    	
			return "forward:/uat/uap/coreLoginPolicy.cm";
		}
	}                             		

	/**
	 * 기 등록된 로그인정책 정보를 수정한다.
	 * @param loginPolicy - 로그인정책 model
	 * @return String - 리턴 Url
	 */
	@RequestMapping("/uat/uap/coreLoginPolicyModify.cm")
	public String coreLoginPolicyModify(@ModelAttribute("loginPolicy") LoginPolicy loginPolicy,
			                         BindingResult bindingResult,
                                     ModelMap model) throws Exception {

		beanValidator.validate(loginPolicy, bindingResult); //validation 수행	
		
    	if (bindingResult.hasErrors()) { 
    		model.addAttribute("loginPolicyVO", loginPolicy);
			return "coreframework/com/uat/uap/CoreLoginPolicyUpdt";
		} else {
			LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
			loginPolicy.setUserId(user == null ? "" : EgovStringUtil.isNullToString(user.getId()));

			coreLoginPolicyService.updateLoginPolicy(loginPolicy);
	    	model.addAttribute("message", coreMessageSource.getMessage("success.common.update"));
	    	
	    	return "forward:/uat/uap/coreLoginPolicyList.cm";
		}
	}

	/**
	 * 기 등록된 로그인정책 정보를 삭제한다.
	 * @param loginPolicy - 로그인정책 model
	 * @return String - 리턴 Url
	 */
	@RequestMapping("/uat/uap/coreLoginPolicyRemove.cm")
	public String coreLoginPolicyRemove(@ModelAttribute("loginPolicy") LoginPolicy loginPolicy,
                                     ModelMap model) throws Exception {

		coreLoginPolicyService.deleteLoginPolicy(loginPolicy);

		model.addAttribute("message", coreMessageSource.getMessage("success.common.delete"));
		return "forward:/uat/uap/coreLoginPolicyList.cm";
	}
}
