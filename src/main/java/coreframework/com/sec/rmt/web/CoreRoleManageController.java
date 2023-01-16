package coreframework.com.sec.rmt.web;

import coreframework.com.cmm.ComDefaultCodeVO;
import coreframework.com.cmm.CoreMessageSource;
import coreframework.com.cmm.SessionVO;
import coreframework.com.cmm.annotation.IncludedInfo;
import coreframework.com.cmm.service.CoreCmmUseService;
import coreframework.com.cmm.util.CoreUtil;
import coreframework.com.sec.ram.mapper.CoreAuthorManageMapper;
import coreframework.com.sec.ram.vo.AuthorManageVO;
import coreframework.com.sec.rmt.service.CoreRoleManageService;
import coreframework.com.sec.rmt.vo.RoleManage;
import coreframework.com.sec.rmt.vo.RoleManageVO;
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
import java.util.List;

/**
 * 롤관리에 관한 controller 클래스를 정의한다.
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
public class CoreRoleManageController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CoreRoleManageController.class);
    @Resource(name="coreMessageSource")
	CoreMessageSource coreMessageSource;

    @Resource(name = "CoreRoleManageService")
    private CoreRoleManageService coreRoleManageService;

    @Resource(name = "CoreCmmUseService")
    CoreCmmUseService coreCmmUseService;

    @Resource(name = "CoreAuthorManageMapper")
    private CoreAuthorManageMapper coreAuthorManageService;

    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;

    /** Message ID Generation */
    @Resource(name="egovRoleIdGnrService")
    private EgovIdGnrService egovRoleIdGnrService;

    @Autowired
	private DefaultBeanValidator beanValidator;

    /**
	 * 롤 목록화면 이동
	 * @return String
	 * @exception Exception
	 */
    @RequestMapping("/sec/rmt/EgovRoleListView.cm")
    public String selectRoleListView()
            throws Exception {
        return "coreframework/com/sec/rmt/CoreRoleManage2";
    }

	/**
	 * 등록된 롤 정보 목록 조회
	 * @param roleManageVO RoleManageVO
	 * @return String
	 * @exception Exception
	 */
    @IncludedInfo(name="롤관리", listUrl="/sec/rmt/CoreRoleList.cm", order = 90,gid = 20)
    @RequestMapping(value="/sec/rmt/CoreRoleList.cm")
	public String selectRoleList(@ModelAttribute("roleManageVO") RoleManageVO roleManageVO,
			                      ModelMap model) throws Exception {

		log.info("RoleManageVO-pageunit : "+ roleManageVO.getPageUnit());
    	/** paging */
    	PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(roleManageVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(roleManageVO.getPageUnit());
		paginationInfo.setPageSize(roleManageVO.getPageSize());

		roleManageVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		roleManageVO.setLastIndex(paginationInfo.getLastRecordIndex());
		roleManageVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		roleManageVO.setRoleManageList(coreRoleManageService.selectRoleList(roleManageVO));
        model.addAttribute("roleList", roleManageVO.getRoleManageList());

        int totCnt = coreRoleManageService.selectRoleListTotCnt(roleManageVO);
		paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);
        model.addAttribute("message", coreMessageSource.getMessage("success.common.select"));

		model.addAttribute("roleListJson", new CoreUtil().modelToJson(roleManageVO.getRoleManageList()));
		model.addAttribute("paginationInfoJson", new CoreUtil().modelToJson(paginationInfo));
		model.addAttribute("messageJson", new CoreUtil().modelToJson(coreMessageSource.getMessage("success.common.select")));

        return "coreframework/com/sec/rmt/CoreRoleManage2";
	}

	/**
	 * 등록된 롤 정보 조회
	 * @param roleCode String
	 * @param roleManageVO RoleManageVO
	 * @param authorManageVO AuthorManageVO
	 * @return String
	 * @exception Exception
	 */
    @RequestMapping(value="/sec/rmt/CoreRole.cm")
	public String selectRole(@RequestParam("roleCode") String roleCode,
	                         @ModelAttribute("roleManageVO") RoleManageVO roleManageVO,
	                         @ModelAttribute("authorManageVO") AuthorManageVO authorManageVO,
		                      ModelMap model) throws Exception {

		log.info("roleManageVO.searchKeyword() : "+roleManageVO.getSearchKeyword());
		log.info("roleManageVO.getRoleCode : "+roleManageVO.getRoleCode());
    	roleManageVO.setRoleCode(roleCode);

    	authorManageVO.setAuthorManageList(coreAuthorManageService.selectAuthorAllList(authorManageVO));

    	model.addAttribute("role", coreRoleManageService.selectRole(roleManageVO));
        model.addAttribute("authorManageList", authorManageVO.getAuthorManageList());
        model.addAttribute("cmmCodeDetailList", getCmmCodeDetailList(new ComDefaultCodeVO(),"COM029"));
		log.info("roleManageVO.getPageIndex() update = "+roleManageVO.getPageIndex());


        return "coreframework/com/sec/rmt/CoreRoleUpdate2";
	}

    /**
	 * 롤 등록화면 이동
	 * @param authorManageVO AuthorManageVO
	 * @return String
	 * @exception Exception
	 */
    @RequestMapping("/sec/rmt/CoreRoleInsertView.cm")
    public String insertRoleView(@ModelAttribute("authorManageVO") AuthorManageVO authorManageVO,
    								@ModelAttribute("roleManage") RoleManage roleManage,
    									ModelMap model) throws Exception {
		log.info("roleManage.getPageIndex() = " + roleManage.getPageIndex());
    	authorManageVO.setAuthorManageList(coreAuthorManageService.selectAuthorAllList(authorManageVO));
        model.addAttribute("authorManageList", authorManageVO.getAuthorManageList());
        model.addAttribute("cmmCodeDetailList", getCmmCodeDetailList(new ComDefaultCodeVO(),"COM029"));
		log.info("roleManage.getPageIndex() = " + roleManage.getPageIndex());

        return "coreframework/com/sec/rmt/CoreRoleInsert2";
    }

    /**
	 * 공통코드 호출
	 * @param comDefaultCodeVO ComDefaultCodeVO
	 * @param codeId String
	 * @return List
	 * @exception Exception
	 */
    public List<?> getCmmCodeDetailList(ComDefaultCodeVO comDefaultCodeVO, String codeId)  throws Exception {
    	comDefaultCodeVO.setCodeId(codeId);
    	return coreCmmUseService.selectCmmCodeDetail(comDefaultCodeVO);
    }

	/**
	 * 시스템 메뉴에 따른 접근권한, 데이터 입력, 수정, 삭제의 권한 롤을 등록
	 * @param roleManage RoleManage
	 * @param roleManageVO RoleManageVO
	 * @return String
	 * @exception Exception
	 */
    @RequestMapping(value="/sec/rmt/CoreRoleInsert.cm")
	public String insertRole(@ModelAttribute("roleManage") RoleManage roleManage,
			                 @ModelAttribute("roleManageVO") RoleManageVO roleManageVO,
                              ModelMap model) throws Exception {
    	    String roleTyp = roleManage.getRoleTyp();
	    	if("method".equals(roleTyp))//KISA 보안약점 조치 (2018-10-29, 윤창원)
	    		roleTyp = "mtd";
	    	else if("pointcut".equals(roleTyp))//KISA 보안약점 조치 (2018-10-29, 윤창원)
	    		roleTyp = "pct";
	    	else roleTyp = "web";

	    	roleManage.setRoleCode(roleTyp.concat("-").concat(egovRoleIdGnrService.getNextStringId()));
	    	roleManageVO.setRoleCode(roleManage.getRoleCode());

	        model.addAttribute("cmmCodeDetailList", getCmmCodeDetailList(new ComDefaultCodeVO(),"COM029"));
	    	model.addAttribute("message", coreMessageSource.getMessage("success.common.insert"));
	        model.addAttribute("roleManage", coreRoleManageService.insertRole(roleManage, roleManageVO));

	        //return "coreframework/com/sec/rmt/CoreRoleUpdate";
	        return "forward:/sec/rmt/CoreRoleList.cm";

	}

	/**
	 * 시스템 메뉴에 따른 접근권한, 데이터 입력, 수정, 삭제의 권한 롤을 수정
	 * @param roleManage RoleManage
	 * @param bindingResult BindingResult
	 * @return String
	 * @exception Exception
	 */
    @RequestMapping(value="/sec/rmt/CoreRoleUpdate.cm")
	public String updateRole(@ModelAttribute("role") RoleManage role,
			BindingResult bindingResult,
            ModelMap model) throws Exception {

    	beanValidator.validate(role, bindingResult); //validation 수행
    	if (bindingResult.hasErrors()) {
			log.info("roleManage if = {}",role.getRoleCode());
			return "coreframework/com/sec/rmt/CoreRoleUpdate";
		} else {
    	coreRoleManageService.updateRole(role);
    	model.addAttribute("message", coreMessageSource.getMessage("success.common.update"));
    	//return "forward:/sec/rmt/CoreRole.cm";


			log.info("roleManage else = {}",role.getRoleCode());
    	return "forward:/sec/rmt/CoreRoleList.cm";
		}
	}

	/**
	 * 불필요한 롤정보를 화면에 조회하여 데이터베이스에서 삭제
	 * @param roleManage RoleManage
	 * @return String
	 * @exception Exception
	 */
    @RequestMapping(value="/sec/rmt/CoreRoleDelete.cm")
	public String deleteRole(@ModelAttribute("roleManage") RoleManage roleManage,
            ModelMap model) throws Exception {
		System.out.println("****************"+roleManage.getRoleCode());
		System.out.println("****************"+roleManage.getRoleNm());
    	coreRoleManageService.deleteRole(roleManage);
    	model.addAttribute("message", coreMessageSource.getMessage("success.common.delete"));
		log.info("roleManage = {}",roleManage.getRoleCode());

		return "forward:/sec/rmt/CoreRoleList.cm";

	}

	/**
	 * 불필요한 그룹정보 목록을 화면에 조회하여 데이터베이스에서 삭제
	 * @param roleCodes String
	 * @param roleManage RoleManage
	 * @return String
	 * @exception Exception
	 */
	@RequestMapping(value="/sec/rmt/CoreRoleListDelete.cm")
	public String deleteRoleList(@RequestParam("roleCodes") String roleCodes,
			                     @ModelAttribute("roleManage") RoleManage roleManage,
	                              Model model) throws Exception {
    	String [] strRoleCodes = roleCodes.split(";");
    	for(int i=0; i<strRoleCodes.length;i++) {
    		roleManage.setRoleCode(strRoleCodes[i]);
    		coreRoleManageService.deleteRole(roleManage);
    	}

		model.addAttribute("message", coreMessageSource.getMessage("success.common.delete"));
		return "forward:/sec/rmt/CoreRoleList.cm";
	}

}