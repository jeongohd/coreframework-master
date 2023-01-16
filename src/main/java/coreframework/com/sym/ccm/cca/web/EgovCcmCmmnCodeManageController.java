package coreframework.com.sym.ccm.cca.web;

import java.util.List;

import javax.annotation.Resource;

import coreframework.com.cmm.util.CoreUserDetailsHelper;
import coreframework.com.sym.ccm.cca.service.CmmnCode;
import coreframework.com.sym.ccm.cca.service.CmmnCodeVO;
import coreframework.com.sym.ccm.cca.service.EgovCcmCmmnCodeManageService;
import coreframework.com.sym.ccm.ccc.service.CcmCmmnClCodeManageService;
import coreframework.com.sym.ccm.ccc.vo.CmmnClCodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springmodules.validation.commons.DefaultBeanValidator;

import coreframework.com.cmm.CoreMessageSource;
import coreframework.com.cmm.LoginVO;
import coreframework.com.cmm.annotation.IncludedInfo;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
*
* 공통코드에 관한 요청을 받아 서비스 클래스로 요청을 전달하고 서비스클래스에서 처리한 결과를 웹 화면으로 전달을 위한 Controller를 정의한다
* @author 공통서비스 개발팀 이중호
* @since 2009.04.01
* @version 1.0
* @see
*
* <pre>
* << 개정이력(Modification Information) >>
*
*   수정일      수정자           수정내용
*  -------    --------    ---------------------------
*   2009.04.01  이중호          최초 생성
*   2011.8.26	정진오			IncludedInfo annotation 추가
*   2017.08.16	이정은	표준프레임워크 v3.7 개선
*
* </pre>
*/

@Controller
public class EgovCcmCmmnCodeManageController {
	
	@Resource(name = "CmmnCodeManageService")
    private EgovCcmCmmnCodeManageService ccmCmmnCodeManageService;

	@Resource(name = "CcmCmmnClCodeManageService")
    private CcmCmmnClCodeManageService ccmCmmnClCodeManageService;
	
	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	/** CoreMessageSource */
	@Resource(name = "coreMessageSource")
    CoreMessageSource coreMessageSource;
	
	@Autowired
	private DefaultBeanValidator beanValidator;
	
	
	
	/**
	 * 공통분류코드 목록을 조회한다.
	 * 
	 * @param searchVO
	 * @param model
	 * @return "coreframework/com/sym/ccm/cca/EgovCcmCmmnCodeList"
	 * @throws Exception
	 */
	@IncludedInfo(name = "공통코드", listUrl = "/sym/ccm/cca/SelectCcmCmmnCodeList.cm", order = 980, gid = 60)
	@RequestMapping(value = "/sym/ccm/cca/SelectCcmCmmnCodeList.cm")
	public String selectCmmnCodeList(@ModelAttribute("searchVO") CmmnCodeVO searchVO, ModelMap model)
			throws Exception {
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

		List<?> CmmnCodeList = ccmCmmnCodeManageService.selectCmmnCodeList(searchVO);
		model.addAttribute("resultList", CmmnCodeList);

		int totCnt = ccmCmmnCodeManageService.selectCmmnCodeListTotCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt);
		model.addAttribute("paginationInfo", paginationInfo);

		return "coreframework/com/sym/ccm/cca/EgovCcmCmmnCodeList";
	}
	
	/**
	 * 공통코드 상세항목을 조회한다.
	 * 
	 * @param loginVO
	 * @param cmmnCodeVO
	 * @param model
	 * @return "coreframework/com/sym/ccm/cca/EgovCcmCmmnCodeDetail"
	 * @throws Exception
	 */
	@RequestMapping(value = "/sym/ccm/cca/SelectCcmCmmnCodeDetail.cm")
	public String selectCmmnCodeDetail(@ModelAttribute("loginVO") LoginVO loginVO, CmmnCodeVO cmmnCodeVO, ModelMap model) throws Exception {
		
		CmmnCodeVO vo = ccmCmmnCodeManageService.selectCmmnCodeDetail(cmmnCodeVO);
		
		model.addAttribute("result", vo);

		return "coreframework/com/sym/ccm/cca/EgovCcmCmmnCodeDetail";
	}
	
	/**
	 * 공통코드 등록을 위한 등록페이지로 이동한다.
	 * 
	 * @param cmmnCodeVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/sym/ccm/cca/RegistCcmCmmnCodeView.cm")
	public String insertCmmnCodeView(@ModelAttribute("cmmnCodeVO")CmmnCodeVO cmmnCodeVO, ModelMap model) throws Exception {
		
		CmmnClCodeVO searchVO = new CmmnClCodeVO();
		searchVO.setFirstIndex(0);
        List<?> CmmnCodeList = ccmCmmnClCodeManageService.selectCmmnClCodeList(searchVO);
        
        model.addAttribute("clCodeList", CmmnCodeList);

		return "coreframework/com/sym/ccm/cca/EgovCcmCmmnCodeRegist";
	}
	
	/**
     * 공통코드를 등록한다.
     * 
     * @param CmmnCodeVO
     * @param CmmnCodeVO
     * @param status
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/sym/ccm/cca/RegistCcmCmmnCode.cm")
    public String insertCmmnCode(@ModelAttribute("searchVO") CmmnCodeVO cmmnCode, @ModelAttribute("cmmnCodeVO") CmmnCodeVO cmmnCodeVO,
	    BindingResult bindingResult, ModelMap model) throws Exception {

		LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
		
		CmmnClCodeVO searchVO = new CmmnClCodeVO();
	
		beanValidator.validate(cmmnCodeVO, bindingResult);
	
		if (bindingResult.hasErrors()) {
			
	        List<?> CmmnCodeList = ccmCmmnClCodeManageService.selectCmmnClCodeList(searchVO);
	        model.addAttribute("clCodeList", CmmnCodeList);
	        
		    return "coreframework/com/sym/ccm/cca/EgovCcmCmmnCodeRegist";
		}
		
		if(cmmnCode.getCodeId() != null){
			CmmnCode vo = ccmCmmnCodeManageService.selectCmmnCodeDetail(cmmnCode);
			if(vo != null){
				model.addAttribute("message", coreMessageSource.getMessage("comSymCcmCca.validate.codeCheck"));
				
				searchVO.setFirstIndex(0);
		        List<?> CmmnCodeList = ccmCmmnClCodeManageService.selectCmmnClCodeList(searchVO);
		        model.addAttribute("clCodeList", CmmnCodeList);
		        
				return "coreframework/com/sym/ccm/cca/EgovCcmCmmnCodeRegist";
			}
		}
	
		cmmnCodeVO.setFrstRegisterId((user == null || user.getUniqId() == null) ? "" : user.getUniqId());
		ccmCmmnCodeManageService.insertCmmnCode(cmmnCodeVO);
	
		return "forward:/sym/ccm/cca/SelectCcmCmmnCodeList.cm";
    }
        
    /**
     * 공통코드를 삭제한다.
     * 
     * @param cmmnCodeVO
     * @param status
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/sym/ccm/cca/RemoveCcmCmmnCode.cm")
    public String deleteCmmnCode(@ModelAttribute("cmmnCodeVO") CmmnCodeVO cmmnCodeVO,
	    BindingResult bindingResult, ModelMap model) throws Exception {

    	LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();

    		cmmnCodeVO.setLastUpdusrId((user == null || user.getUniqId() == null) ? "" : user.getUniqId());
		ccmCmmnCodeManageService.deleteCmmnCode(cmmnCodeVO);

    		return "forward:/sym/ccm/cca/SelectCcmCmmnCodeList.cm";
        }
    
    /**
     * 공통코드 수정을 위한 수정페이지로 이동한다.
     * 
     * @param cmmnCodeVO
     * @param model
     * @return "coreframework/com/sym/ccm/cca/EgovCcmCmmnCodeUpdt"
     * @throws Exception
     */
    @RequestMapping("/sym/ccm/cca/UpdateCcmCmmnCodeView.cm")
    public String updateCmmnCodeView(@ModelAttribute("cmmnCodeVO") CmmnCodeVO cmmnCodeVO, ModelMap model)
	    throws Exception {
		
    	CmmnCode result = ccmCmmnCodeManageService.selectCmmnCodeDetail(cmmnCodeVO);
		
		model.addAttribute("cmmnCodeVO", result);
	
		return "coreframework/com/sym/ccm/cca/EgovCcmCmmnCodeUpdt";
    }
    
    /**
     * 공통코드를 수정한다.
     * 
     * @param cmmnCodeVO
     * @param status
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/sym/ccm/cca/UpdateCcmCmmnCode.cm")
    public String updateCmmnCode(@ModelAttribute("searchVO") CmmnCodeVO cmmnCode, @ModelAttribute("cmmnCodeVO") CmmnCodeVO cmmnCodeVO,
	    BindingResult bindingResult, ModelMap model) throws Exception {

				LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
	
		beanValidator.validate(cmmnCodeVO, bindingResult);
		if (bindingResult.hasErrors()) {
	
			CmmnCode result = ccmCmmnCodeManageService.selectCmmnCodeDetail(cmmnCode);
		    model.addAttribute("cmmnCodeVO", result);
	
		    return "coreframework/com/sym/ccm/cca/EgovCcmCmmnCodeUpdt";
		}
	
		cmmnCodeVO.setLastUpdusrId((user == null || user.getUniqId() == null) ? "" : user.getUniqId());
		ccmCmmnCodeManageService.updateCmmnCode(cmmnCodeVO);

		return "forward:/sym/ccm/cca/SelectCcmCmmnCodeList.cm";
    }
	
	
}