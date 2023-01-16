package coreframework.com.uss.olh.hpc.web;

import java.util.List;

import javax.annotation.Resource;

import coreframework.com.utl.fcc.service.EgovStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springmodules.validation.commons.DefaultBeanValidator;

import coreframework.com.cmm.ComDefaultCodeVO;
import coreframework.com.cmm.CoreMessageSource;
import coreframework.com.cmm.LoginVO;
import coreframework.com.cmm.annotation.IncludedInfo;
import coreframework.com.cmm.service.CoreCmmUseService;
import coreframework.com.cmm.util.CoreUserDetailsHelper;
import coreframework.com.uss.olh.hpc.service.EgovHpcmService;
import coreframework.com.uss.olh.hpc.service.HpcmVO;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
*
* 도움말을 처리하는 비즈니스 구현 클래스
* @author 공통서비스 개발팀 박정규
* @since 2009.04.01
* @version 1.0
* @see
*
* <pre>
* << 개정이력(Modification Information) >>
*
*   수정일      수정자           수정내용
*  -------    --------    ---------------------------
*   2009.04.01  박정규          최초 생성
*   2011.8.26	 정진오			IncludedInfo annotation 추가
*   2011.09.28  서준식          return값 오류 수정
*   2016.08.02  김연호          표준프레임워크 3.6 개선
* </pre>
*/

@Controller
public class EgovHpcmController {
	
	@Resource(name = "EgovHpcmService")
    private EgovHpcmService egovHpcmService;

    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;

	@Resource(name="CoreCmmUseService")
	private CoreCmmUseService cmmUseService;

	/** CoreMessageSource */
    @Resource(name="coreMessageSource")
    CoreMessageSource coreMessageSource;

    // Validation 관련
	@Autowired
	private DefaultBeanValidator beanValidator;
	
	 /**
     * 도움말내용 목록을 조회한다. (pageing)
     * @param searchVO
     * @param model
     * @return	"/uss/olh/hpc/EgovHpcmList"
     * @throws Exception
     */
    @IncludedInfo(name="도움말", order = 520 ,gid = 50)
    @RequestMapping(value="/uss/olh/hpc/selectHpcmList.cm")
    public String selectHpcmList(@ModelAttribute("searchVO") HpcmVO searchVO, ModelMap model) throws Exception {

    	/** EgovPropertyService.SiteList */
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

        List<?> HpcmList = egovHpcmService.selectHpcmList(searchVO);
        model.addAttribute("resultList", HpcmList);

        int totCnt = egovHpcmService.selectHpcmListCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);

        return "coreframework/com/uss/olh/hpc/EgovHpcmList";
    }
    
    /**
     * 도움말내용 목록에 대한 상세정보를 조회한다.
     * @param hpcmManageVO
     * @param searchVO
     * @param model
     * @return	"/uss/olh/hpc/EgovHpcmDetailInqire"
     * @throws Exception
     */
    @RequestMapping("/uss/olh/hpc/selectHpcmDetail.cm")
    public String	selectHpcmDetail(HpcmVO hpcmManageVO, @ModelAttribute("searchVO") HpcmVO searchVO,
            ModelMap model) throws Exception {

		HpcmVO vo = egovHpcmService.selectHpcmDetail(hpcmManageVO);

		model.addAttribute("result", vo);

        return	"coreframework/com/uss/olh/hpc/EgovHpcmDetail";
    }
    
    /**
     * 도움말내용를 등록하기 위한 전 처리(공통코드 처리)
     * @param searchVO
     * @param model
     * @return	"/uss/olh/hpc/EgovHpcmCnRegist"
     * @throws Exception
     */
    @RequestMapping("/uss/olh/hpc/insertHpcmView.cm")
    public String insertHpcmView(
            @ModelAttribute("searchVO") HpcmVO searchVO, Model model) throws Exception {

    	// 공통코드를 가져오기 위한 Vo
    	ComDefaultCodeVO vo = new ComDefaultCodeVO();
		vo.setCodeId("COM021");

		List<?> _result = cmmUseService.selectCmmCodeDetail(vo);
		model.addAttribute("hpcmSeCode", _result);

        model.addAttribute("hpcmVO", new HpcmVO());

        return "coreframework/com/uss/olh/hpc/EgovHpcmRegist";

    }
    
    /**
     * 도움말내용를 등록한다.
     * @param searchVO
     * @param hpcmVO
     * @param bindingResult
     * @return	"forward:/uss/olh/hpc/selectHpcmList.cm"
     * @throws Exception
     */
    @RequestMapping("/uss/olh/hpc/insertHpcm.cm")
    public String insertHpcmCn(
            @ModelAttribute("searchVO") HpcmVO searchVO,  @ModelAttribute("hpcmVO") HpcmVO hpcmVO,
            BindingResult bindingResult) throws Exception {

    	beanValidator.validate(hpcmVO, bindingResult);
		if(bindingResult.hasErrors()){
			return "coreframework/com/uss/olh/hpc/EgovHpcmRegist";
		}

    	// 로그인VO에서  사용자 정보 가져오기
    	LoginVO	loginVO = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();

    	String frstRegisterId = loginVO == null ? "" : EgovStringUtil.isNullToString(loginVO.getUniqId());

    	hpcmVO.setFrstRegisterId(frstRegisterId);		// 최초등록자ID
    	hpcmVO.setLastUpdusrId(frstRegisterId);    	// 최종수정자ID

        egovHpcmService.insertHpcm(hpcmVO);

        return "forward:/uss/olh/hpc/selectHpcmList.cm";
    }
    
    /**
     * 도움말내용를 수정하기 위한 전 처리(공통코드 처리)
     * @param hpcmId
     * @param searchVO
     * @param model
     * @return	"/uss/olh/hpc/EgovHpcmUpdt"
     * @throws Exception
     */
    @RequestMapping("/uss/olh/hpc/updateHpcmView.cm")
    public String updateHpcmView(@RequestParam("hpcmId") String hpcmId ,
            @ModelAttribute("searchVO") HpcmVO searchVO, ModelMap model)
            throws Exception {

    	// 공통코드를 가져오기 위한 Vo
    	ComDefaultCodeVO vo = new ComDefaultCodeVO();
		vo.setCodeId("COM021");

		List<?> _result = cmmUseService.selectCmmCodeDetail(vo);
		model.addAttribute("hpcmSeCode", _result);

        HpcmVO hpcmVO = new HpcmVO();
        hpcmVO.setHpcmId(hpcmId);

        model.addAttribute("hpcmVO", egovHpcmService.selectHpcmDetail(hpcmVO));

        return "coreframework/com/uss/olh/hpc/EgovHpcmUpdt";
    }
    
    /**
     * 도움말을 수정한다.
     * @param searchVO
     * @param hpcmVO
     * @param bindingResult
     * @return	"forward:/uss/olh/hpc/selectHpcmList.cm"
     * @throws Exception
     */
    @RequestMapping("/uss/olh/hpc/updateHpcm.cm")
    public String updateHpcm(
            @ModelAttribute("searchVO") HpcmVO searchVO,
            @ModelAttribute("hpcmManageVO") HpcmVO hpcmVO,
            BindingResult bindingResult)
            throws Exception {

    	// Validation
    	beanValidator.validate(hpcmVO, bindingResult);
		if(bindingResult.hasErrors()){
			return "coreframework/com/uss/olh/hpc/EgovHpcmUpdt";
		}

    	LoginVO	loginVO = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
    	String	lastUpdusrId = loginVO == null ? "" : EgovStringUtil.isNullToString(loginVO.getUniqId());

    	hpcmVO.setLastUpdusrId(lastUpdusrId);    	// 최종수정자ID
    	egovHpcmService.updateHpcm(hpcmVO);


        return "forward:/uss/olh/hpc/selectHpcmList.cm";

    }
    
    /**
     * 도움말을 삭제한다.
     * @param hpcmVO
     * @param searchVO
     * @return	"forward:/uss/olh/hpc/selectHpcmList.cm"
     * @throws Exception
     */
    @RequestMapping("/uss/olh/hpc/deleteHpcm.cm")
    public String deleteHpcmCn(HpcmVO hpcmVO, @ModelAttribute("searchVO") HpcmVO searchVO)
            throws Exception {

    	egovHpcmService.deleteHpcmCn(hpcmVO);

        return "forward:/uss/olh/hpc/selectHpcmList.cm";
    }
    
}
