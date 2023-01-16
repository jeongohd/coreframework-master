package coreframework.com.uss.umt.api;

import coreframework.com.cmm.ComUrlVO;
import coreframework.com.cmm.CoreMessageSource;
import coreframework.com.cmm.annotation.IncludedInfo;
import coreframework.com.cmm.util.ApiResult;
import coreframework.com.cmm.util.CoreUserDetailsHelper;
import coreframework.com.uss.umt.service.CoreDeptManageService;
import coreframework.com.uss.umt.vo.DeptManageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springmodules.validation.commons.DefaultBeanValidator;

import javax.annotation.Resource;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * 부서관련 처리를  비지니스 클래스로 전달하고 처리된결과를  해당   웹 화면으로 전달하는  Controller를 정의한다
 * @author 공통서비스 개발팀 조재영
 * @since 2009.00.00
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.02.01  lee.m.j     최초 생성
 *   2015.06.16  조정국      서비스 화면 접근시 조회결과를 표시하도록 수정
 *   2021.05.30  정진오      로그인인증제한
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class CoreDeptManageApi {
	private static final Logger LOGGER = LoggerFactory.getLogger(CoreDeptManageApi.class);
	@Resource(name = "coreMessageSource")
    CoreMessageSource coreMessageSource;

	@Resource(name = "CoreDeptManageService")
	private CoreDeptManageService coreDeptManageMapper;

	/** Message ID Generation */
	@Resource(name = "egovDeptManageIdGnrService")
	private EgovIdGnrService egovDeptManageIdGnrService;

	@Autowired
	private DefaultBeanValidator beanValidator;

	/**
	 * 부서 목록화면 이동
	 * @return String
	 * @exception Exception
	 */
	@IncludedInfo(name = "부서관리", order = 461, gid = 50)
	@RequestMapping("/uss/umt/dpt/selectDeptManageListViewApi.cm")
	public ApiResult<Object> selectDeptManageListViewApi(ComUrlVO comUrlVO) throws Exception {

		// 2021.05.30, 정진오, 로그인인증제한
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			comUrlVO.setSelfUrl("index");
			try {
				return ApiResult.Data(comUrlVO,  null);
			}catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
			//return "index";
		}

		comUrlVO.setFwdUrl("/uss/umt/dpt/selectDeptManageListApi.cm");
		try {
			return ApiResult.Data(comUrlVO,  null);
		}catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}

		//return "forward:/uss/umt/dpt/selectDeptManageListApi.cm";
	}

	/**
	 * 부서를 관리하기 위해 등록된 부서목록을 조회한다.
	 * @param bannerVO - 배너 VO
	 * @return String - 리턴 URL
	 * @throws Exception
	 */

	@RequestMapping(value = "/uss/umt/dpt/selectDeptManageListApi.cm")
	public ApiResult<Object> selectDeptManageListApi(@ModelAttribute("deptManageVO") DeptManageVO deptManageVO, ModelMap model) throws Exception {

		/** paging */
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(deptManageVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(deptManageVO.getPageUnit());
		paginationInfo.setPageSize(deptManageVO.getPageSize());

		deptManageVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		deptManageVO.setLastIndex(paginationInfo.getLastRecordIndex());
		deptManageVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		model.addAttribute("deptManageList", coreDeptManageMapper.selectDeptManageList(deptManageVO));

		int totCnt = coreDeptManageMapper.selectDeptManageListTotCnt(deptManageVO);
		paginationInfo.setTotalRecordCount(totCnt);
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("message", coreMessageSource.getMessage("success.common.select"));

		try {
			return ApiResult.OK(coreDeptManageMapper.selectDeptManageList(deptManageVO), paginationInfo, null);
		}catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}

		//return "coreframework/com/uss/umt/CoreDeptManageList";
	}

	/**
	 * 등록된 부서의 상세정보를 조회한다.
	 * @param bannerVO - 부서 Vo
	 * @return String - 리턴 Url
	 */

	@RequestMapping(value = "/uss/umt/dpt/getDeptManageApi.cm")
	public ApiResult<Object> selectDeptManageApi(@RequestParam("orgnztId") String orgnztId, @ModelAttribute("deptManageVO") DeptManageVO deptManageVO, ModelMap model) throws Exception {

		deptManageVO.setOrgnztId(orgnztId);

		model.addAttribute("deptManage", coreDeptManageMapper.selectDeptManage(deptManageVO));
		model.addAttribute("message", coreMessageSource.getMessage("success.common.select"));

		try {
			return ApiResult.Data(coreDeptManageMapper.selectDeptManage(deptManageVO),  null);
		}catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}

		//return "coreframework/com/uss/umt/CoreDeptManageUpdt";
	}

	/**
	 * 부서등록 화면으로 이동한다.
	 * @param banner - 부서 model
	 * @return String - 리턴 Url
	 */
	@RequestMapping(value = "/uss/umt/dpt/addViewDeptManageApi.cm")
	public ApiResult<Object> insertViewDeptManageApi(@ModelAttribute("deptManageVO") DeptManageVO deptManageVO, ModelMap model) throws Exception {

		model.addAttribute("deptManage", deptManageVO);
		//return "coreframework/com/uss/umt/CoreDeptManageInsert";

		try {
			return ApiResult.Data(deptManageVO, null);
		}catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
	}

	/**
	 * 부서정보를 신규로 등록한다.
	 * @param banner - 부서 model
	 * @return String - 리턴 Url
	 */
	@RequestMapping(value = "/uss/umt/dpt/addDeptManageApi.cm")
	public ApiResult<Object> insertDeptManageApi(@ModelAttribute("deptManageVO") DeptManageVO deptManageVO,
											  ComUrlVO comUrlVO,
											  BindingResult bindingResult,  ModelMap model) throws Exception {

		beanValidator.validate(deptManageVO, bindingResult); //validation 수행

		deptManageVO.setOrgnztId(egovDeptManageIdGnrService.getNextStringId());

		if (bindingResult.hasErrors()) {
			//return "coreframework/com/uss/umt/CoreDeptManageInsert";
			comUrlVO.setSelfUrl("coreframework/com/uss/umt/CoreDeptManageInsert");
			try {
				return ApiResult.Data(comUrlVO,  null);
			}catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}

		} else {
			coreDeptManageMapper.insertDeptManage(deptManageVO);
			model.addAttribute("message", coreMessageSource.getMessage("success.common.insert"));
			//comUrlVO.setSelfUrl("coreframework/com/uss/umt/CoreDeptManageInsert");
			try {
				return ApiResult.Message(coreMessageSource.getMessage("success.common.insert"));
			}catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
			//return "forward:/uss/umt/dpt/selectDeptManageListApi.cm";
		}
	}

	/**
	 * 기 등록된 부서정보를 수정한다.
	 * @param banner - 부서 model
	 * @return String - 리턴 Url
	 */
	@RequestMapping(value = "/uss/umt/dpt/updtDeptManageApi.cm")
	public ApiResult<Object> updateDeptManageApi(@ModelAttribute("deptManageVO") DeptManageVO deptManageVO,
											  ComUrlVO comUrlVO,
											  BindingResult bindingResult, ModelMap model) throws Exception {
		beanValidator.validate(deptManageVO, bindingResult); //validation 수행

		if (bindingResult.hasErrors()) {
			//return "coreframework/com/uss/umt/CoreDeptManageUpdt";
			comUrlVO.setSelfUrl("coreframework/com/uss/umt/CoreDeptManageUpdt");
			try {
				return ApiResult.Data(comUrlVO,  null);
			}catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}

		} else {
			coreDeptManageMapper.updateDeptManage(deptManageVO);
			model.addAttribute("message", coreMessageSource.getMessage("success.common.insert"));
			//return "forward:/uss/umt/dpt/selectDeptManageListApi.cm";
			try {
				return ApiResult.Message(coreMessageSource.getMessage("success.common.insert"));
			}catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
		}
	}

	/**
	 * 기 등록된 부서정보를 삭제한다.
	 * @param banner Banner
	 * @return String
	 * @exception Exception
	 */
	@RequestMapping(value = "/uss/umt/dpt/removeDeptManageApi.cm")
	public ApiResult<Object> deleteDeptManageApi(@ModelAttribute("deptManageVO") DeptManageVO deptManageVO,
											  ComUrlVO comUrlVO,
											  Model model) throws Exception {

		coreDeptManageMapper.deleteDeptManage(deptManageVO);
		model.addAttribute("message", coreMessageSource.getMessage("success.common.delete"));

		try {
			return ApiResult.Message(coreMessageSource.getMessage("success.common.delete"));
		}catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}

		//return "forward:/uss/umt/dpt/selectDeptManageListApi.cm";
	}

	/**
	 * 기 등록된 부서정보목록을 일괄 삭제한다.
	 * @param banners String
	 * @param banner Banner
	 * @return String
	 * @exception Exception
	 */
	@RequestMapping(value = "/uss/umt/dpt/removeDeptManageListApi.cm")
	public ApiResult<Object> deleteDeptManageList(@RequestParam("deptManages") String deptManages, @ModelAttribute("deptManageVO") DeptManageVO deptManageVO, ModelMap model) throws Exception {

		String[] strDeptManages = deptManages.split(";");
		for (int i = 0; i < strDeptManages.length; i++) {
			deptManageVO.setOrgnztId(strDeptManages[i]);
			coreDeptManageMapper.deleteDeptManage(deptManageVO);
		}
		
		model.addAttribute("message", coreMessageSource.getMessage("success.common.delete"));

		try {
			return ApiResult.Message(coreMessageSource.getMessage("success.common.delete"));
		}catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
		//return "forward:/uss/umt/dpt/selectDeptManageListApi.cm";
	}

}
