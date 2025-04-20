import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const UpdateMemberPage = () => {
    const navigate = useNavigate();

    // 상태 변수들
    const [memberPw, setMemberPw] = useState('');
    const [memberNickname, setMemberNickname] = useState('');
    const [memberBirthday, setMemberBirthday] = useState('');
    const [memberPhoneNumber, setMemberPhoneNumber] = useState('');

    // 세션 정보 가져오기
    const [userData, setUserData] = useState({
        memberId: '',
        memberName: '',
        memberNickname: '',
        memberBirthday: '',
        memberPhoneNumber: '',
        memberAuth: '',
    });

    // 세션 정보 가져오기 (회원정보 수정 페이지에서)
    useEffect(() => {
        const memberId = sessionStorage.getItem('memberId');
        const memberName = sessionStorage.getItem('memberName');
        const memberNickname = sessionStorage.getItem('memberNickname');
        const memberBirthday = sessionStorage.getItem('memberBirthday');
        const memberPhoneNumber = sessionStorage.getItem('memberPhoneNumber');
        const memberAuth = sessionStorage.getItem('memberAuth');

        // 세션 정보 설정
        setUserData({
            memberId,
            memberName,
            memberNickname,
            memberBirthday,
            memberPhoneNumber,
            memberAuth,
        });

        // 입력값에 세션 정보로 초기화
        setMemberNickname(memberNickname || '');
        setMemberBirthday(memberBirthday || '');
        setMemberPhoneNumber(memberPhoneNumber || '');
    }, []);

    // 회원정보 수정 폼 제출 처리 함수
    const handleSubmit = async (e) => {
        e.preventDefault();

        const memberId = sessionStorage.getItem('memberId'); // memberId from sessionStorage

        const requestData = {
            memberId,       // Adding memberId to the request data
            memberPw,
            memberNickname,
            memberBirthday,
            memberPhoneNumber,
        };

        try {
            // PUT 요청 보내기 (여기서 API 주소는 변경해야 합니다)
            const response = await axios.put('http://localhost:8080/member/v1/memberUpdate', requestData);
            console.log(response.data);  // 응답 처리
            console.log(response.status);  // 응답 처리

            // 응답 처리 후 마이페이지로 이동
            if (response.status === 200) {  // 응답이 성공적일 경우
                console.log(response.data);  // 응답 처리
                navigate('/mypage');  // 마이페이지로 이동
            } else {
                console.error('회원 정보 수정에 실패했습니다1111');
            }
        } catch (error) {
            console.error('회원 정보 수정에 실패했습니다2222', error);
        }
    };

    // 마이페이지로 돌아가기
    const handleNavigateToMyPage = () => {
        navigate('/mypage');
    };

    return (
        <div className="update-member-container">
            <h2>회원 정보 수정</h2>

            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="memberPw">비밀번호</label>
                    <input
                        type="password"
                        id="memberPw"
                        value={memberPw}
                        onChange={(e) => setMemberPw(e.target.value)}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="memberNickname">닉네임</label>
                    <input
                        type="text"
                        id="memberNickname"
                        value={memberNickname}
                        onChange={(e) => setMemberNickname(e.target.value)}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="memberBirthday">생일</label>
                    <input
                        type="text"
                        id="memberBirthday"
                        value={memberBirthday}
                        onChange={(e) => setMemberBirthday(e.target.value)}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="memberPhoneNumber">전화번호</label>
                    <input
                        type="tel"
                        id="memberPhoneNumber"
                        value={memberPhoneNumber}
                        onChange={(e) => setMemberPhoneNumber(e.target.value)}
                    />
                </div>

                <button type="submit">정보 수정</button>
            </form>

            {/* 취소 버튼 */}
            <button onClick={handleNavigateToMyPage}>취소하기</button>
        </div>
    );
};

export default UpdateMemberPage;
