package coreframework.com.uss.umt.service;

import coreframework.com.uss.umt.mapper.CoreDeptManageMapper;
import coreframework.com.uss.umt.vo.DeptManageVO;
import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("CoreDeptManageService")
public class CoreDeptManageService extends EgovAbstractMapper {
	
	@Resource(name="CoreDeptManageMapper")
    private CoreDeptManageMapper coreDeptManageMapper;

	/**
	 * 부서를 관리하기 위해 등록된 부서목록을 조회한다.
	 * @param deptManageVO - 부서 Vo
	 * @return List - 부서 목록
	 * 
	 * @param deptManageVO
	 */
	public List<DeptManageVO> selectDeptManageList(DeptManageVO deptManageVO) throws Exception {
		return coreDeptManageMapper.selectDeptManageList(deptManageVO);
	}

	/**
	 * 부서목록 총 갯수를 조회한다.
	 * @param deptManageVO - 부서 Vo
	 * @return int - 부서 카운트 수
	 * 
	 * @param deptManageVO
	 */
	public int selectDeptManageListTotCnt(DeptManageVO deptManageVO) throws Exception {
		return coreDeptManageMapper.selectDeptManageListTotCnt(deptManageVO);
	}

	/**
	 * 등록된 부서의 상세정보를 조회한다.
	 * @param deptManageVO - 부서 Vo
	 * @return deptManageVO - 부서 Vo
	 * 
	 * @param deptManageVO
	 */
	public DeptManageVO selectDeptManage(DeptManageVO deptManageVO) throws Exception {
		return coreDeptManageMapper.selectDeptManage(deptManageVO);
	}

	/**
	 * 부서정보를 신규로 등록한다.
	 * @param deptManageVO - 부서 model
	 * 
	 * @param deptManageVO
	 */
	public void insertDeptManage(DeptManageVO deptManageVO) throws Exception {
		coreDeptManageMapper.insertDeptManage(deptManageVO);
	}

	/**
	 * 기 등록된 부서정보를 수정한다.
	 * @param deptManageVO - 부서 model
	 * 
	 * @param deptManageVO
	 */
	public void updateDeptManage(DeptManageVO deptManageVO) throws Exception {
		coreDeptManageMapper.updateDeptManage(deptManageVO);
	}

	/**
	 * 기 등록된 부서정보를 삭제한다.
	 * @param deptManageVO - 부서 model
	 * 
	 * @param deptManageVO
	 */
	public void deleteDeptManage(DeptManageVO deptManageVO) throws Exception {
		coreDeptManageMapper.deleteDeptManage(deptManageVO);
	}
}
