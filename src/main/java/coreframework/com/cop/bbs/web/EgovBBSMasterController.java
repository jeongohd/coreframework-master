package coreframework.com.cop.bbs.web;

import coreframework.com.cmm.ComDefaultCodeVO;
import coreframework.com.cmm.CoreComponentChecker;
import coreframework.com.cmm.CoreMessageSource;
import coreframework.com.cmm.LoginVO;
import coreframework.com.cmm.annotation.IncludedInfo;
import coreframework.com.cmm.service.CoreCmmUseService;
import coreframework.com.cmm.util.CoreUserDetailsHelper;
import coreframework.com.cop.bbs.service.*;
import coreframework.com.utl.fcc.service.EgovStringUtil;
import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springmodules.validation.commons.DefaultBeanValidator;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * 게시판 속성관리를 위한 컨트롤러  클래스
 * @author 공통서비스개발팀 이삼섭
 * @since 2009.06.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------       --------    ---------------------------
 *   2009.3.12  이삼섭          최초 생성
 *   2009.06.26	한성곤		    2단계 기능 추가 (댓글관리, 만족도조사)
 *	 2011.07.21 안민정          커뮤니티 관련 메소드 분리 (->EgovBBSAttributeManageController)
 *	 2011.8.26	정진오			IncludedInfo annotation 추가
 *   2011.09.15 서준식           2단계 기능 추가 (댓글관리, 만족도조사) 적용방법 변경
 *   2016.06.13 김연호          표준프레임워크 v3.6 개선
 * </pre>
 */

@Controller
public class EgovBBSMasterController {

    @Resource(name = "EgovBBSMasterService")
    private EgovBBSMasterService egovBBSMasterService;

    @Resource(name = "CoreCmmUseService")
    private CoreCmmUseService cmmUseService;

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;
    
    @Resource(name = "egovBBSMstrIdGnrService")
    private EgovIdGnrService idgenServiceBbs;
    
    @Resource(name = "egovBlogIdGnrService")
    private EgovIdGnrService idgenServiceBlog;
    
    /** CoreMessageSource */
	@Resource(name = "coreMessageSource")
    CoreMessageSource coreMessageSource;
    


    @Autowired
    private DefaultBeanValidator beanValidator;

    //Logger log = Logger.getLogger(this.getClass());
    
    /**
     * 신규 게시판 마스터 등록을 위한 등록페이지로 이동한다.
     * 
     * @param boardMasterVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/insertBBSMasterView.cm")
    public String insertBBSMasterView(@ModelAttribute("searchVO") BoardMasterVO boardMasterVO, ModelMap model) throws Exception {
		BoardMasterVO boardMaster = new BoardMasterVO();
		//공통코드(게시판유형)
		ComDefaultCodeVO vo = new ComDefaultCodeVO();
		vo.setCodeId("COM101");
		List<?> codeResult = cmmUseService.selectCmmCodeDetail(vo);
		model.addAttribute("bbsTyCode", codeResult);
		model.addAttribute("boardMasterVO", boardMaster);
	
	
		//---------------------------------
		// 2011.09.15 : 2단계 기능 추가 반영 방법 변경
		//---------------------------------

		
		if(CoreComponentChecker.hasComponent("EgovArticleCommentService")){
			model.addAttribute("useComment", "true");
		}
		if(CoreComponentChecker.hasComponent("EgovBBSSatisfactionService")){
			model.addAttribute("useSatisfaction", "true");
		}
		
		return "coreframework/com/cop/bbs/EgovBBSMasterRegist";
    }

    /**
     * 신규 게시판 마스터 정보를 등록한다.
     * 
     * @param boardMasterVO
     * @param boardMaster
     * @param status
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/insertBBSMaster.cm")
    public String insertBBSMaster(@ModelAttribute("searchVO") BoardMasterVO boardMasterVO, @ModelAttribute("boardMaster") BoardMaster boardMaster,
	    BindingResult bindingResult, ModelMap model) throws Exception {
    	
		LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		
		beanValidator.validate(boardMaster, bindingResult);
		if (bindingResult.hasErrors()) {
		    ComDefaultCodeVO vo = new ComDefaultCodeVO();
		    
		    //게시판유형코드
		    vo.setCodeId("COM101");
		    List<?> codeResult = cmmUseService.selectCmmCodeDetail(vo);
		    model.addAttribute("bbsTyCode", codeResult);
	
		    return "coreframework/com/cop/bbs/EgovBBSMasterRegist";
		}
		
		if (isAuthenticated) {
		    boardMaster.setFrstRegisterId(user == null ? "" : EgovStringUtil.isNullToString(user.getUniqId()));
		    if((boardMasterVO == null ? "" : EgovStringUtil.isNullToString(boardMasterVO.getBlogAt())).equals("Y")){
		    	boardMaster.setBlogAt("Y");
		    }else{
		    	boardMaster.setBlogAt("N");
		    }
		    egovBBSMasterService.insertBBSMasterInf(boardMaster);
		}
		if(boardMaster.getBlogAt().equals("Y")){
			return "forward:/cop/bbs/selectArticleBlogList.cm";
		}else{
			return "forward:/cop/bbs/selectBBSMasterInfs.cm";
		}
		
    }

    /**
     * 게시판 마스터 목록을 조회한다.
     * 
     * @param boardMasterVO
     * @param model
     * @return
     * @throws Exception
     */
    @IncludedInfo(name="게시판관리",order = 180 ,gid = 40)
    @RequestMapping("/cop/bbs/selectBBSMasterInfs.cm")
    public String selectBBSMasterInfs(@ModelAttribute("searchVO") BoardMasterVO boardMasterVO, ModelMap model) throws Exception {
		boardMasterVO.setPageUnit(propertyService.getInt("pageUnit"));
		boardMasterVO.setPageSize(propertyService.getInt("pageSize"));
	
		PaginationInfo paginationInfo = new PaginationInfo();
		
		paginationInfo.setCurrentPageNo(boardMasterVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(boardMasterVO.getPageUnit());
		paginationInfo.setPageSize(boardMasterVO.getPageSize());
	
		boardMasterVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		boardMasterVO.setLastIndex(paginationInfo.getLastRecordIndex());
		boardMasterVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
	
		Map<String, Object> map = egovBBSMasterService.selectBBSMasterInfs(boardMasterVO);
		int totCnt = Integer.parseInt((String)map.get("resultCnt"));
		
		paginationInfo.setTotalRecordCount(totCnt);
	
		model.addAttribute("resultList", map.get("resultList"));
		model.addAttribute("resultCnt", map.get("resultCnt"));	
		model.addAttribute("paginationInfo", paginationInfo);

		return "coreframework/com/cop/bbs/EgovBBSMasterList";
    }
    
    /**
     * 블로그에 대한 목록을 조회한다.
     * 
     * @param blogVO
     * @param model
     * @return
     * @throws Exception
     */
    @IncludedInfo(name="블로그관리", order = 170 ,gid = 40)
    @RequestMapping("/cop/bbs/selectBlogList.cm")
    public String selectBlogMasterList(@ModelAttribute("searchVO") BoardMasterVO boardMasterVO, ModelMap model) throws Exception {
    	
    	LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
    	 //KISA 보안취약점 조치 (2018-12-10, 신용호)
        Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();

        if(!isAuthenticated) {
            return "coreframework/com/uat/uia/CoreLoginUsr";
        }
    	
		boardMasterVO.setPageUnit(propertyService.getInt("pageUnit"));
		boardMasterVO.setPageSize(propertyService.getInt("pageSize"));
	
		PaginationInfo paginationInfo = new PaginationInfo();
		
		paginationInfo.setCurrentPageNo(boardMasterVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(boardMasterVO.getPageUnit());
		paginationInfo.setPageSize(boardMasterVO.getPageSize());
	
		boardMasterVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		boardMasterVO.setLastIndex(paginationInfo.getLastRecordIndex());
		boardMasterVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		boardMasterVO.setFrstRegisterId(user == null ? "" : EgovStringUtil.isNullToString(user.getUniqId()));
		
		Map<String, Object> map = egovBBSMasterService.selectBlogMasterInfs(boardMasterVO);
		int totCnt = Integer.parseInt((String)map.get("resultCnt"));
		
		paginationInfo.setTotalRecordCount(totCnt);
	
		model.addAttribute("resultList", map.get("resultList"));
		model.addAttribute("resultCnt", map.get("resultCnt"));	
		model.addAttribute("paginationInfo", paginationInfo);

		return "coreframework/com/cop/bbs/EgovBlogList";
    }
    
    /**
     * 블로그 등록을 위한 등록페이지로 이동한다.
     * 
     * @param blogVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/insertBlogMasterView.cm")
    public String insertBlogMasterView(@ModelAttribute("searchVO") BlogVO blogVO, ModelMap model) throws Exception {
    	model.addAttribute("blogMasterVO", new BlogVO());
	return "coreframework/com/cop/bbs/EgovBlogRegist";
    }
    
    /**
     * 블로그 생성 유무를 판단한다.
     * 
     * @param blogVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/selectChkBloguser.cm")
    public ModelAndView chkBlogUser(@ModelAttribute("searchVO") BlogVO blogVO, ModelMap model) throws Exception {
    	LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
   	 	// KISA 보안취약점 조치 (2018-12-10, 신용호)
        Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();

        if(!isAuthenticated) {
        	throw new IllegalAccessException("Login Required!");
        }
    	
    	model.addAttribute("blogMasterVO", new BlogVO());
    	
    	String userVal="";
    	blogVO.setFrstRegisterId(user == null ? "" : EgovStringUtil.isNullToString(user.getUniqId()));
    	userVal = egovBBSMasterService.checkBlogUser(blogVO);
    	
    	ModelAndView mav = new ModelAndView("jsonView");
    	mav.addObject("userChk", userVal);
    	return mav;
    }

    /**
     * 블로그 정보를 등록한다.
     * 
     * @param blogVO
     * @param blog
     * @param status
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/insertBlogMaster.cm")
    public String insertBlogMaster(@ModelAttribute("searchVO") BlogVO blogVO, @ModelAttribute("blogMaster") Blog blog,
	    BindingResult bindingResult, ModelMap model) throws Exception {

		LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		
        if(!isAuthenticated) { //KISA 보안약점 조치 (2018-12-10, 신용호)
            return "coreframework/com/uat/uia/CoreLoginUsr";
        }
		
		blogVO.setFrstRegisterId(user == null ? "" : EgovStringUtil.isNullToString(user.getUniqId()));
		BlogVO vo = egovBBSMasterService.checkBlogUser2(blogVO);
		
		if(vo != null) {
			model.addAttribute("blogMasterVO", new BlogVO());
			model.addAttribute("message", coreMessageSource.getMessage("comCopBlog.validate.blogUserCheck"));
			return "coreframework/com/cop/bbs/EgovBlogRegist";
		}
		
		beanValidator.validate(blog, bindingResult);
		
		if (bindingResult.hasErrors()) {
		    return "coreframework/com/cop/bbs/EgovBlogRegist";
		}
		
		String blogId = idgenServiceBlog.getNextStringId(); //블로그 아이디 채번
		String bbsId = idgenServiceBbs.getNextStringId(); //게시판 아이디 채번
		
		blog.setRegistSeCode("REGC02");
		blog.setFrstRegisterId(user == null ? "" : EgovStringUtil.isNullToString(user.getUniqId()));
		blog.setBbsId(bbsId);
		blog.setBlogId(blogId);
		blog.setBlogAt("Y");
		egovBBSMasterService.insertBlogMaster(blog);
		
		if (isAuthenticated) {
		    //블로그 개설자의 정보를 등록한다.
		    BlogUserVO blogUserVO = new BlogUserVO();
		    blogUserVO.setBlogId(blogId);
		    blogUserVO.setEmplyrId(user == null ? "" : EgovStringUtil.isNullToString(user.getUniqId()));
		    blogUserVO.setMngrAt("Y");
		    blogUserVO.setMberSttus("P");
		    blogUserVO.setUseAt("Y");
		    blogUserVO.setFrstRegisterId(user == null ? "" : EgovStringUtil.isNullToString(user.getUniqId()));
		    
		    egovBBSMasterService.insertBoardBlogUserRqst(blogUserVO);
		}
		return "forward:/cop/bbs/selectBlogList.cm";
    }

    /**
     * 게시판 마스터 상세내용을 조회한다.
     * 
     * @param boardMasterVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/selectBBSMasterDetail.cm")
    public String selectBBSMasterDetail(@ModelAttribute("searchVO") BoardMasterVO searchVO, ModelMap model) throws Exception {
		BoardMasterVO vo = egovBBSMasterService.selectBBSMasterInf(searchVO);
		model.addAttribute("result", vo);
	
		//---------------------------------
		// 2011.09.15 : 2단계 기능 추가 반영 방법 변경
		//---------------------------------
		
		if(CoreComponentChecker.hasComponent("EgovArticleCommentService")){
			model.addAttribute("useComment", "true");
		}
		if(CoreComponentChecker.hasComponent("EgovBBSSatisfactionService")){
			model.addAttribute("useSatisfaction", "true");
		}
		
		return "coreframework/com/cop/bbs/EgovBBSMasterDetail";
    }
    
    /**
     * 게시판 마스터정보를 수정하기 위한 전 처리
     * @param bbsId
     * @param searchVO
     * @param model
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/updateBBSMasterView.cm")
    public String updateBBSMasterView(@RequestParam("bbsId") String bbsId ,
            @ModelAttribute("searchVO") BoardMaster searchVO, ModelMap model)
            throws Exception {


        BoardMasterVO boardMasterVO = new BoardMasterVO();

        
        //게시판유형코드
        ComDefaultCodeVO vo = new ComDefaultCodeVO();
        vo.setCodeId("COM101");
        List<?> codeResult = cmmUseService.selectCmmCodeDetail(vo);
        model.addAttribute("bbsTyCode", codeResult);
        
        // Primary Key 값 세팅
        boardMasterVO.setBbsId(bbsId);

        model.addAttribute("boardMasterVO", egovBBSMasterService.selectBBSMasterInf(boardMasterVO));

		//---------------------------------
		// 2011.09.15 : 2단계 기능 추가 반영 방법 변경
		//---------------------------------
		
		if(CoreComponentChecker.hasComponent("EgovArticleCommentService")){
			model.addAttribute("useComment", "true");
		}
		if(CoreComponentChecker.hasComponent("EgovBBSSatisfactionService")){
			model.addAttribute("useSatisfaction", "true");
		}
        
        return "coreframework/com/cop/bbs/EgovBBSMasterUpdt";
    }
    

    /**
     * 게시판 마스터 정보를 수정한다.
     * 
     * @param boardMasterVO
     * @param boardMaster
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/updateBBSMaster.cm")
    public String updateBBSMaster(@ModelAttribute("searchVO") BoardMasterVO boardMasterVO, @ModelAttribute("boardMaster") BoardMaster boardMaster,
	    BindingResult bindingResult, ModelMap model) throws Exception {

		LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
	
		beanValidator.validate(boardMaster, bindingResult);
		if (bindingResult.hasErrors()) {
		    BoardMasterVO vo = egovBBSMasterService.selectBBSMasterInf(boardMasterVO);
	
		    model.addAttribute("result", vo);
	
		    ComDefaultCodeVO comVo = new ComDefaultCodeVO();
	        comVo.setCodeId("COM101");
	        List<?> codeResult = cmmUseService.selectCmmCodeDetail(comVo);
	        model.addAttribute("bbsTyCode", codeResult);
		    
		    return "coreframework/com/cop/bbs/EgovBBSMasterUpdt";
		}
	
		if (isAuthenticated) {
		    boardMaster.setLastUpdusrId(user == null ? "" : EgovStringUtil.isNullToString(user.getUniqId()));
		    egovBBSMasterService.updateBBSMasterInf(boardMaster);
		}
	
		return "forward:/cop/bbs/selectBBSMasterInfs.cm";
    }

    /**
     * 게시판 마스터 정보를 삭제한다.
     * 
     * @param boardMasterVO
     * @param boardMaster
     * @param status
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/deleteBBSMaster.cm")
    public String deleteBBSMaster(@ModelAttribute("searchVO") BoardMasterVO boardMasterVO, @ModelAttribute("boardMaster") BoardMaster boardMaster
	    ) throws Exception {

	LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
	Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();

	if (isAuthenticated) {
	    boardMaster.setLastUpdusrId(user == null ? "" : EgovStringUtil.isNullToString(user.getUniqId()));
	    egovBBSMasterService.deleteBBSMasterInf(boardMaster);
	}
	// status.setComplete();
	return "forward:/cop/bbs/selectBBSMasterInfs.cm";
    }
    
    /**
     * 포트릿을 위한 블로그 목록 정보를 조회한다.
     * 
     * @param blogVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/selectBlogListPortlet.cm")
    public String selectBlogListPortlet(@ModelAttribute("searchVO") BlogVO blogVO, ModelMap model) throws Exception {
	List<BlogVO> result = egovBBSMasterService.selectBlogListPortlet(blogVO);
	
	model.addAttribute("resultList", result);

	return "coreframework/com/cop/bbs/EgovBlogListPortlet";
    }
    
    /**
     * 포트릿을 위한 게시판 목록 정보를 조회한다.
     * 
     * @param blogVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/cop/bbs/selectBBSListPortlet.cm")
    public String selectBBSListPortlet(@ModelAttribute("searchVO") BoardMasterVO boardMasterVO, ModelMap model) throws Exception {
    	List<BoardMasterVO> result = egovBBSMasterService.selectBBSListPortlet(boardMasterVO);
    	
    	model.addAttribute("resultList", result);
    	
    	return "coreframework/com/cop/bbs/EgovBBSListPortlet";
    }

    
}
