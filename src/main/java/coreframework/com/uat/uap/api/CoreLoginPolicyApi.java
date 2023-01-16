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

package coreframework.com.uat.uap.api;

import coreframework.com.cmm.CoreMessageSource;
import coreframework.com.cmm.LoginVO;
import coreframework.com.cmm.annotation.IncludedInfo;
import coreframework.com.cmm.util.ApiResult;
import coreframework.com.cmm.util.CoreUserDetailsHelper;
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
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springmodules.validation.commons.DefaultBeanValidator;

import javax.annotation.Resource;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin("*") // 모든 요청에 접근 허용
public class CoreLoginPolicyApi {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoreLoginPolicyApi.class);
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
	@RequestMapping("/uat/uap/selectLoginPolicyListViewApi.cm")
	public ApiResult<Object> selectLoginPolicyListViewApi(LoginVO loginVO) throws Exception {
		try {
			//return "coreframework/com/uat/uap/CoreLoginPolicyList";
			loginVO.setUrl("coreframework/com/uat/uap/CoreLoginPolicyList");
			return ApiResult.Data( loginVO,null);
		} catch (Exception e) {
			log.error(e.getMessage());
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}

	}	

	/**
	 * 로그인정책 목록을 조회한다.
	 * @param loginPolicyVO - 로그인정책 VO
	 * @return String - 리턴 Url
	 */
	@IncludedInfo(name="로그인정책관리", order = 30 ,gid = 10)
	@RequestMapping("/uat/uap/selectLoginPolicyListApi.cm")
	public ApiResult<Object> selectLoginPolicyListApi(@ModelAttribute("loginPolicyVO") LoginPolicyVO loginPolicyVO,
			                             ModelMap model) throws Exception {
		
    	/** paging */
    	PaginationInfo paginationInfo = new PaginationInfo();
	    paginationInfo.setCurrentPageNo(loginPolicyVO.getPageIndex());
	    paginationInfo.setRecordCountPerPage(loginPolicyVO.getPageUnit());
	    paginationInfo.setPageSize(loginPolicyVO.getPageSize());
		
	    loginPolicyVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
	    loginPolicyVO.setLastIndex(paginationInfo.getLastRecordIndex());
	    loginPolicyVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		try {
			loginPolicyVO.setLoginPolicyList(coreLoginPolicyService.selectLoginPolicyList(loginPolicyVO));
			model.addAttribute("loginPolicyList", loginPolicyVO.getLoginPolicyList());

			int totCnt = coreLoginPolicyService.selectLoginPolicyListTotCnt(loginPolicyVO);
			paginationInfo.setTotalRecordCount(totCnt);
//			model.addAttribute("paginationInfo", paginationInfo);
//			model.addAttribute("message", coreMessageSource.getMessage("success.common.select"));

			return ApiResult.OK(loginPolicyVO.getLoginPolicyList(),paginationInfo, coreMessageSource.getMessage("success.common.select"));

		} catch (Exception e){

			log.error(e.getMessage());
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
	}

	/**
	 * 로그인정책 목록의 상세정보를 조회한다.
	 * @param loginPolicyVO - 로그인정책 VO
	 * @return String - 리턴 Url
	 */
	@RequestMapping("/uat/uap/coreLoginPolicyApi.cm")
	public ApiResult<Object> selectLoginPolicyApi(@RequestParam("emplyrId") String emplyrId,
			                        @ModelAttribute("loginPolicyVO") LoginPolicyVO loginPolicyVO,
                                     ModelMap model) throws Exception {
		
		loginPolicyVO.setEmplyrId(emplyrId);

		model.addAttribute("loginPolicy", coreLoginPolicyService.selectLoginPolicy(loginPolicyVO));
		model.addAttribute("message", coreMessageSource.getMessage("success.common.select"));

		LoginPolicyVO vo = (LoginPolicyVO)model.get("loginPolicy");

		try {
			if (vo.getRegYn().equals("N"))	//신규
//			return "coreframework/com/uat/uap/CoreLoginPolicyRegist";
				return ApiResult.Data(coreLoginPolicyService.selectLoginPolicy(loginPolicyVO), coreMessageSource.getMessage("success.common.select"));
			else
//			return "coreframework/com/uat/uap/CoreLoginPolicyUpdt";
				return ApiResult.Data(coreLoginPolicyService.selectLoginPolicy(loginPolicyVO), coreMessageSource.getMessage("success.common.select"));
		}catch (Exception e) {
			log.error(e.getMessage());
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
	}

	/**
	 * 로그인정책 정보 등록화면으로 이동한다.
	 * @param loginPolicy - 로그인정책 model
	 * @return String - 리턴 Url
	 */
	@RequestMapping("/uat/uap/coreLoginPolicyRegistApi.cm")
	public ApiResult<Object> insertLoginPolicyViewApi(@RequestParam("emplyrId") String emplyrId,
                                        @ModelAttribute("loginPolicyVO") LoginPolicyVO loginPolicyVO, 
                                         ModelMap model) throws Exception {
		
		loginPolicyVO.setEmplyrId(emplyrId);
		
//		model.addAttribute("loginPolicy", coreLoginPolicyService.selectLoginPolicy(loginPolicyVO));
//		model.addAttribute("message", coreMessageSource.getMessage("success.common.select"));
//		return "coreframework/com/uat/uap/CoreLoginPolicyRegist";
		try {
			return ApiResult.List(coreLoginPolicyService.selectLoginPolicy(loginPolicyVO), coreMessageSource.getMessage("success.common.select"));
		} catch (Exception e) {
			log.error(e.getMessage());
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}


	}

	/**
	 * 로그인정책 정보를 신규로 등록한다.
	 * @param loginPolicy - 로그인정책 model
	 * @return String - 리턴 Url
	 */
	@RequestMapping("/uat/uap/coreLoginPolicyAddApi.cm")
	public ApiResult<Object> coreLoginPolicyAddApi(@ModelAttribute("loginPolicy") LoginPolicy loginPolicy,
			                         BindingResult bindingResult,
                                     ModelMap model) throws Exception {
		
		beanValidator.validate(loginPolicy, bindingResult); //validation 수행		
		
    	if (bindingResult.hasErrors()) { 
//    		model.addAttribute("loginPolicyVO", loginPolicy);
//			return "coreframework/com/uat/uap/CoreLoginPolicyRegist";
			try {
				return ApiResult.Data( loginPolicy, null);
			} catch (Exception e) {
				log.error(e.getMessage());
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
		} else {
			
			LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
			loginPolicy.setUserId(user == null ? "" : EgovStringUtil.isNullToString(user.getId()));


//	    	model.addAttribute("message", coreMessageSource.getMessage("success.common.update"));
//			return "forward:/uat/uap/coreLoginPolicy.cm";
			try {
				coreLoginPolicyService.insertLoginPolicy(loginPolicy);
				return ApiResult.Message(coreMessageSource.getMessage("success.common.update"));

			} catch (Exception e) {
				log.error(e.getMessage());
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
		}
	}                             		

	/**
	 * 기 등록된 로그인정책 정보를 수정한다.
	 * @param loginPolicy - 로그인정책 model
	 * @return String - 리턴 Url
	 */
	@RequestMapping("/uat/uap/coreLoginPolicyModifyApi.cm")
	public ApiResult<Object> coreLoginPolicyModifyApi(@ModelAttribute("loginPolicy") LoginPolicy loginPolicy,
			                         BindingResult bindingResult,
                                     ModelMap model) throws Exception {
		
		beanValidator.validate(loginPolicy, bindingResult); //validation 수행	
		
    	if (bindingResult.hasErrors()) { 
//    		model.addAttribute("loginPolicyVO", loginPolicy);
//			return "coreframework/com/uat/uap/CoreLoginPolicyUpdt";
			String message = coreMessageSource.getMessageArgs("errors.required", new Object[]{bindingResult.getFieldError().getField()});

			try {
				return ApiResult.Data(loginPolicy, message);
			} catch (Exception e) {
				log.error(e.getMessage());
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
		} else {
			LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
			loginPolicy.setUserId(user == null ? "" : EgovStringUtil.isNullToString(user.getId()));


//	    	model.addAttribute("message", coreMessageSource.getMessage("success.common.update"));
//	    	return "forward:/uat/uap/coreLoginPolicyList.cm";

			try {
				coreLoginPolicyService.updateLoginPolicy(loginPolicy);
				return ApiResult.Message(coreMessageSource.getMessage("success.common.update"));
			} catch (Exception e) {
				log.error(e.getMessage());
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}

		}
	}

	/**
	 * 기 등록된 로그인정책 정보를 삭제한다.
	 * @param loginPolicy - 로그인정책 model
	 * @return String - 리턴 Url
	 */
	@RequestMapping("/uat/uap/coreLoginPolicyRemoveApi.cm")
	public ApiResult<Object> deleteLoginPolicy(@ModelAttribute("loginPolicy") LoginPolicy loginPolicy,
                                     ModelMap model) throws Exception {



//		model.addAttribute("message", coreMessageSource.getMessage("success.common.delete"));
//		return "forward:/uat/uap/coreLoginPolicyList.cm";

		try {
			coreLoginPolicyService.deleteLoginPolicy(loginPolicy);
			return ApiResult.Message(coreMessageSource.getMessage("success.common.delete"));
		} catch (Exception e) {
			log.error(e.getMessage());
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
	}
}
