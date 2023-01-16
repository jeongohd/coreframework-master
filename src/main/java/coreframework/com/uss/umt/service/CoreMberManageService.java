package coreframework.com.uss.umt.service;

import coreframework.com.uss.umt.mapper.CoreEntrprsManageMapper;
import coreframework.com.uss.umt.mapper.CoreMberManageMapper;
import coreframework.com.uss.umt.mapper.CoreUserManageMapper;
import coreframework.com.uss.umt.vo.MberManageVO;
import coreframework.com.uss.umt.vo.UserDefaultVO;
import coreframework.com.utl.fcc.service.EgovStringUtil;
import coreframework.com.utl.sim.service.EgovFileScrty;
import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 일반회원관리에 관한비지니스클래스를 정의한다.
 * @author 공통서비스 개발팀 조재영
 * @since 2009.04.10
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.04.10  조재영          최초 생성
 *   2014.12.08	 이기하			암호화방식 변경(EgovFileScrty.encryptPassword)
 *   2017.07.21  장동한 			로그인인증제한 작업
 *
 * </pre>
 */
@Service("CoreMberManageService")
public class CoreMberManageService extends EgovAbstractMapper {

	/** coreUserManageMapper */
	@Resource(name="CoreUserManageMapper")
	private CoreUserManageMapper coreUserManageMapper;

	/** coreMberManageMapper */
	@Resource(name="CoreMberManageMapper")
	private CoreMberManageMapper coreMberManageMapper;

	/** coreEntrprsManageService */
	@Resource(name="CoreEntrprsManageMapper")
	private CoreEntrprsManageMapper coreEntrprsManageMapper;

	/** egovUsrCnfrmIdGnrService */
	@Resource(name="egovUsrCnfrmIdGnrService")
	private EgovIdGnrService idgenService;

	/**
	 * 사용자의 기본정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
	 * @param mberManageVO 일반회원 등록정보
	 * @return result 등록결과
	 * @throws Exception
	 */
	
	public String insertMber(MberManageVO mberManageVO) throws Exception  {
		//고유아이디 셋팅
		String uniqId = idgenService.getNextStringId();
		mberManageVO.setUniqId(uniqId);
		//패스워드 암호화
		String pass = EgovFileScrty.encryptPassword(mberManageVO.getPassword(), EgovStringUtil.isNullToString(mberManageVO.getMberId()));//KISA 보안약점 조치 (2018-10-29, 윤창원)
		mberManageVO.setPassword(pass);

		String result = coreMberManageMapper.insertMber(mberManageVO);
		return result;
	}

	/**
	 * 기 등록된 사용자 중 검색조건에 맞는 일반회원의 정보를 데이터베이스에서 읽어와 화면에 출력
	 * @param uniqId 상세조회대상 일반회원아이디
	 * @return mberManageVO 일반회원상세정보
	 * @throws Exception
	 */
	
	public MberManageVO selectMber(String uniqId) throws Exception {
		MberManageVO mberManageVO = coreMberManageMapper.selectMber(uniqId);
		return mberManageVO;
	}

	/**
	 * 기 등록된 회원 중 검색조건에 맞는 회원들의 정보를 데이터베이스에서 읽어와 화면에 출력
	 * @param userSearchVO 검색조건
	 * @return List<MberManageVO> 일반회원목록정보
	 */
	
	public List<MberManageVO> selectMberList(UserDefaultVO userSearchVO) throws Exception {
		return coreMberManageMapper.selectMberList(userSearchVO);
	}

    /**
     * 일반회원 총 갯수를 조회한다.
     * @param userSearchVO 검색조건
     * @return 일반회원총갯수(int)
     */
    
	public int selectMberListTotCnt(UserDefaultVO userSearchVO) throws Exception {
    	return coreMberManageMapper.selectMberListTotCnt(userSearchVO);
    }

	/**
	 * 화면에 조회된 일반회원의 기본정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param mberManageVO 일반회원수정정보
	 * @throws Exception
	 */
	
	public void updateMber(MberManageVO mberManageVO) throws Exception {
		//패스워드 암호화
		String pass = EgovFileScrty.encryptPassword(mberManageVO.getPassword(), EgovStringUtil.isNullToString(mberManageVO.getMberId()));//KISA 보안약점 조치 (2018-10-29, 윤창원)
		mberManageVO.setPassword(pass);
		coreMberManageMapper.updateMber(mberManageVO);
	}

	/**
	 * 화면에 조회된 사용자의 정보를 데이터베이스에서 삭제
	 * @param checkedIdForDel 삭제대상 일반회원아이디
	 * @throws Exception
	 */
	
	public void deleteMber(String checkedIdForDel) throws Exception {
		String [] delId = checkedIdForDel.split(",");
		for (int i=0; i<delId.length ; i++){
			String [] id = delId[i].split(":");
			if (id[0].equals("USR03")){
		        //업무사용자(직원)삭제
				coreUserManageMapper.deleteUser(id[1]);
			}else if(id[0].equals("USR01")){
				//일반회원삭제
				coreMberManageMapper.deleteMber(id[1]);
			}else if(id[0].equals("USR02")){
				//기업회원삭제
				coreEntrprsManageMapper.deleteEntrprsmber(id[1]);
			}
		}
	}

	/**
	 * 일반회원 약관확인
	 * @param stplatId 일반회원약관아이디
	 * @return 일반회원약관정보(List)
	 * @throws Exception
	 */
	
	public List<?> selectStplat(String stplatId) throws Exception {
        return coreMberManageMapper.selectStplat(stplatId);
	}

	/**
	 * 일반회원암호수정
	 * @param mberManageVO 일반회원수정정보(비밀번호)
	 * @throws Exception
	 */
	
	public void updatePassword(MberManageVO mberManageVO) throws Exception {
		coreMberManageMapper.updatePassword(mberManageVO);
	}

	/**
	 * 일반회원이 비밀번호를 기억하지 못할 때 비밀번호를 찾을 수 있도록 함
	 * @param passVO 일반회원암호 조회조건정보
	 * @return mberManageVO 일반회원암호정보
	 * @throws Exception
	 */
	
	public MberManageVO selectPassword(MberManageVO passVO) throws Exception {
		MberManageVO mberManageVO = coreMberManageMapper.selectPassword(passVO);
		return mberManageVO;
	}
	
	
	/**
	 * 로그인인증제한 해제 
	 * @param mberManageVO 일반회원정보
	 * @return void
	 * @throws Exception
	 */
	
	public void updateLockIncorrect(MberManageVO mberManageVO) throws Exception {
		coreMberManageMapper.updateLockIncorrect(mberManageVO);
	}

}