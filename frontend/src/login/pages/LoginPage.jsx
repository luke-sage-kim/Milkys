import React, { useState } from 'react'; // useState 추가
import { useNavigate } from 'react-router-dom'; // useNavigate 가져오기
import './LoginPage.css';
import { loginUser } from '../services/authService'; // authService에서 loginUser 함수 가져오기
import axios from 'axios'; // axios 추가 (세션 조회 API 호출을 위해)

function LoginPage() {
  const navigate = useNavigate();

  // 상태 관리: 입력값과 에러 메시지
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const [memberNickname, setMemberNickname] = useState('');
  const [memberData, setMemberData] = useState(null); // 상태에 서버 응답 데이터 저장

  // 입력값 변경 처리 함수
  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name === 'username') setUsername(value);
    if (name === 'password') setPassword(value);
  };

  // 로그인 요청 처리 함수
  const handleLoginSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await loginUser(username, password);

        console.log(response.status)
        console.log( response)
        console.log( response.resultData)
      // 로그인 성공 여부 확인
      if (response.status === 200) {
        sessionStorage.setItem('memberId', username);

        const memberDto = response.resultData; // resultData가 서버 응답에 포함됨

        setMemberData(memberDto); // 상태에 사용자 정보를 저장

        // 세션에 사용자 정보 저장
        sessionStorage.setItem('memberCode', memberDto.memberCode);
        sessionStorage.setItem('memberId', memberDto.memberId);
        sessionStorage.setItem('memberName', memberDto.memberName);
        sessionStorage.setItem('memberNickname', memberDto.memberNickname);
        sessionStorage.setItem('memberBirthday', memberDto.memberBirthday);
        sessionStorage.setItem('memberAuth', memberDto.memberAuth);
        sessionStorage.setItem('memberPhoneNumber', memberDto.memberPhoneNumber);

        console.log('로그인 성공:', 'success');
        console.log('회원 데이터:', memberDto);
        // 대시보드 페이지로 이동
        navigate('/'); // 성공 시 대시보드 페이지로 이동
      } else {
        if(response.resultData ==='UNAPPROVAL'){
          setErrorMessage('승인되지않은 계정입니다. 관리자에게 승인요청해주세요');
        }else{
          // 로그인 실패 시 에러 메시지 처리
          setErrorMessage('로그인에 실패했습니다. 다시 시도해주세요.1');
        }

      }
    } catch (error) {
      setErrorMessage('로그인에 실패했습니다. 다시 시도해주세요.2');
    }
  };

  // 메인 페이지로 이동
  const handleCancelClick = () => {
    navigate('/'); // 메인 페이지로 이동
  };

  // 회원가입 페이지로 이동
  const handleSignUpClick = () => {
    navigate('/signup'); // 회원가입 페이지로 이동
  };

  // 아이디 찾기 페이지로 이동
  const handleFindIdClick = () => {
    navigate('/findId'); // 아이디 찾기 페이지로 이동
  };

  // 아이디 찾기 페이지로 이동
  const handleFindPwClick = () => {
    navigate('/findPw'); // 비밀번호 찾기 페이지로 이동
  };

  return (
      <div className="login-page">
        <h2>로그인</h2>
        <form className="login-form" onSubmit={handleLoginSubmit}>
          <label>
            ID:
            <input
                type="text"
                name="username"
                value={username}
                onChange={handleChange}
                required
            />
          </label>
          <label>
            Password:
            <input
                type="password"
                name="password"
                value={password}
                onChange={handleChange}
                required
            />
          </label>
          {errorMessage && <p className="error-message">{errorMessage}</p>}
          <div className="button-group">
            <button type="submit">로그인하기</button>
            <button type="button" onClick={handleCancelClick}>취소</button>
            <button type="button" onClick={handleSignUpClick}>회원가입</button>
            <button type="button" onClick={handleFindIdClick}>아이디 찾기</button>
            <button type="button" onClick={handleFindPwClick}>비밀번호 찾기</button>
          </div>
        </form>
      </div>
  );
}

export default LoginPage;
