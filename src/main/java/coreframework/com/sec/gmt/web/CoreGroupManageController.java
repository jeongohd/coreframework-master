package coreframework.com.sec.gmt.web;

import coreframework.com.cmm.CoreMessageSource;
import coreframework.com.cmm.SessionVO;
import coreframework.com.cmm.annotation.IncludedInfo;
import coreframework.com.sec.gmt.service.CoreGroupManageService;
import coreframework.com.sec.gmt.vo.GroupManage;
import coreframework.com.sec.gmt.vo.GroupManageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springmodules.validation.commons.DefaultBeanValidator;

import javax.annotation.Resource;

/**
 * 그룹관리에 관한 controller 클래스를 정의한다.
 * @author 공통서비스 개발팀 이문준
 * @since 2009.06.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.03.11  이문준          최초 생성
 *   2011.8.26	정진오			IncludedInfo annotation 추가
 *
 * </pre>
 */

@Slf4j
@RequiredArgsConstructor
@Controller
@SessionAttributes(types=SessionVO.class)
public class CoreGroupManageController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoreGroupManageController.class);
    @Resource(name="coreMessageSource")
    CoreMessageSource coreMessageSource;

    @Resource(name = "CoreGroupManageService")
    private CoreGroupManageService coreGroupManageService;

    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;
	
    /** Message ID Generation */
    @Resource(name="egovGroupIdGnrService")    
    private EgovIdGnrService egovGroupIdGnrService;
    
    @Autowired
	private DefaultBeanValidator beanValidator;
    
    /**
	 * 그룹 목록화면 이동
	 * @return String
	 * @exception Exception
	 */
    @RequestMapping("/sec/gmt/CoreGroupListView.cm")
    public String selectGroupListView()
            throws Exception {
        return "coreframework/com/sec/gmt/CoreGroupManage";
    }   

	/**
	 * 시스템사용 목적별 그룹 목록 조회
	 * @param groupManageVO GroupManageVO
	 * @return String
	 * @exception Exception
	 */
    @IncludedInfo(name="그룹관리", listUrl="/sec/gmt/CoreGroupList.cm", order = 80,gid = 20)
    @RequestMapping(value="/sec/gmt/CoreGroupList.cm")
	public String selectGroupList(@ModelAttribute("groupManageVO") GroupManageVO groupManageVO,
                                   ModelMap model) throws Exception {
    	/** paging */
    	PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(groupManageVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(groupManageVO.getPageUnit());
		paginationInfo.setPageSize(groupManageVO.getPageSize());
		
		groupManageVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		groupManageVO.setLastIndex(paginationInfo.getLastRecordIndex());
		groupManageVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
		groupManageVO.setGroupManageList(coreGroupManageService.selectGroupList(groupManageVO));
        model.addAttribute("groupList", groupManageVO.getGroupManageList());
        
        int totCnt = coreGroupManageService.selectGroupListTotCnt(groupManageVO);
		paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);
        model.addAttribute("message", coreMessageSource.getMessage("success.common.select"));

        return "coreframework/com/sec/gmt/CoreGroupManage";
	}

	/**
	 * 검색조건에 따른 그룹정보를 조회
	 * @param groupManageVO GroupManageVO
	 * @return String
	 * @exception Exception
	 */
    @RequestMapping(value="/sec/gmt/CoreGroup.cm")
	public String selectGroup(@ModelAttribute("groupManageVO") GroupManageVO groupManageVO, 
								@ModelAttribute("groupManage") GroupManage groupManage,
	    		               ModelMap model) throws Exception {

	    model.addAttribute("groupManage", coreGroupManageService.selectGroup(groupManageVO));
	    return "coreframework/com/sec/gmt/CoreGroupUpdate";
	}

    /**
	 * 그룹 등록화면 이동
	 * @return String
	 * @exception Exception
	 */     
    @RequestMapping(value="/sec/gmt/CoreGroupInsertView.cm")
    public String insertGroupView(@ModelAttribute("groupManage") GroupManage groupManage)
            throws Exception {
        return "coreframework/com/sec/gmt/CoreGroupInsert";
    }

	/**
	 * 그룹 기본정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
	 * @param groupManage GroupManage
	 * @param groupManageVO GroupManageVO
	 * @return String
	 * @exception Exception
	 */ 
    @RequestMapping(value="/sec/gmt/CoreGroupInsert.cm")
	public String insertGroup(@ModelAttribute("groupManage") GroupManage groupManage, 
			                  @ModelAttribute("groupManageVO") GroupManageVO groupManageVO, 
			                   BindingResult bindingResult,
			                   ModelMap model) throws Exception {
    	
    	beanValidator.validate(groupManage, bindingResult); //validation 수행
    	
    	if (bindingResult.hasErrors()) { 
			return "coreframework/com/sec/gmt/CoreGroupInsert";
		} else {
	    	groupManage.setGroupId(egovGroupIdGnrService.getNextStringId());
	        groupManageVO.setGroupId(groupManage.getGroupId());
	        
	        model.addAttribute("message", coreMessageSource.getMessage("success.common.insert"));
	        model.addAttribute("groupManage", coreGroupManageService.insertGroup(groupManage, groupManageVO));
	        return "forward:/sec/gmt/CoreGroupList.cm";
		}
	}
    
	/**
	 * 화면에 조회된 그룹의 기본정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param groupManage GroupManage
	 * @return String
	 * @exception Exception
	 */     
    @RequestMapping(value="/sec/gmt/CoreGroupUpdate.cm")
	public String updateGroup(@ModelAttribute("groupManage") GroupManage groupManage, 
			                   BindingResult bindingResult,
                               Model model) throws Exception {
    	
    	beanValidator.validate(groupManage, bindingResult); //validation 수행
    	
    	if (bindingResult.hasErrors()) { 
			return "coreframework/com/sec/gmt/CoreGroupUpdate";
		} else {
			coreGroupManageService.updateGroup(groupManage);
            model.addAttribute("message", coreMessageSource.getMessage("success.common.update"));
    	    return "forward:/sec/gmt/CoreGroupList.cm";
		}
	}	
	
	/**
	 * 불필요한 그룹정보를 화면에 조회하여 데이터베이스에서 삭제
	 * @param groupManage GroupManage
	 * @return String
	 * @exception Exception
	 */
	@RequestMapping(value="/sec/gmt/CoreGroupDelete.cm")
	public String deleteGroup(@ModelAttribute("groupManage") GroupManage groupManage, 
                             Model model) throws Exception {
		coreGroupManageService.deleteGroup(groupManage);
		model.addAttribute("message", coreMessageSource.getMessage("success.common.delete"));
		return "forward:/sec/gmt/CoreGroupList.cm";
	}

	/**
	 * 불필요한 그룹정보 목록을 화면에 조회하여 데이터베이스에서 삭제
	 * @param groupIds String
	 * @param groupManage GroupManage
	 * @return String
	 * @exception Exception
	 */   
	@RequestMapping(value="/sec/gmt/CoreGroupListDelete.cm")
	public String deleteGroupList(@RequestParam("groupIds") String groupIds,
			                      @ModelAttribute("groupManage") GroupManage groupManage, 
	                               Model model) throws Exception {
    	String [] strGroupIds = groupIds.split(";");
    	for(int i=0; i<strGroupIds.length;i++) {
    		groupManage.setGroupId(strGroupIds[i]);
			coreGroupManageService.deleteGroup(groupManage);
    	}

		model.addAttribute("message", coreMessageSource.getMessage("success.common.delete"));
		return "forward:/sec/gmt/CoreGroupList.cm";
	}
	
    /**
	 * 그룹팝업 화면 이동
	 * @return String
	 * @exception Exception
	 */
    @RequestMapping("/sec/gmt/CoreGroupSearchView.cm")
    public String selectGroupSearchView()
            throws Exception {
        return "coreframework/com/sec/gmt/CoreGroupSearch";
    }   
	    
	/**
	 * 시스템사용 목적별 그룹 목록 조회
	 * @param groupManageVO GroupManageVO
	 * @return String
	 * @exception Exception
	 */
    @RequestMapping(value="/sec/gmt/CoreGroupSearchList.cm")
	public String selectGroupSearchList(@ModelAttribute("groupManageVO") GroupManageVO groupManageVO, 
                                   ModelMap model) throws Exception {
    	/** paging */
    	PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(groupManageVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(groupManageVO.getPageUnit());
		paginationInfo.setPageSize(groupManageVO.getPageSize());
		
		groupManageVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		groupManageVO.setLastIndex(paginationInfo.getLastRecordIndex());
		groupManageVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
		groupManageVO.setGroupManageList(coreGroupManageService.selectGroupList(groupManageVO));
        model.addAttribute("groupList", groupManageVO.getGroupManageList());
        
        int totCnt = coreGroupManageService.selectGroupListTotCnt(groupManageVO);
		paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);
        model.addAttribute("message", coreMessageSource.getMessage("success.common.select"));

        return "coreframework/com/sec/gmt/CoreGroupSearch";
	}
}