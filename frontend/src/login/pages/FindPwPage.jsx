import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function FindPwPage() {
    const navigate = useNavigate(); // useNavigate 훅을 사용하여 로그인 페이지로 이동

    const [memberId, setMemberId] = useState('');
    const [memberBirthday, setMemberBirthday] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    // 비밀번호 초기화 폼 제출 처리 함수
    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!memberId || !memberBirthday) {
            setError('아이디와 생년월일을 모두 입력해주세요.');
            return;
        }

        setLoading(true);
        try {
            // 서버에 PUT 요청을 보내 비밀번호 초기화
            const response = await axios.put('http://localhost:8080/member/v1/initialPw', {
                memberId: memberId,  // DTO에서 memberId
                memberBirthday: memberBirthday  // DTO에서 memberBirthday
            });

            console.log('서버 응답:', response.data.resultData.resultData);
            const result = response.data.resultData.resultData;
            // 응답 데이터에 따라 처리
            if ( result === 'idError') {
                setError('해당 아이디가 존재하지 않습니다.');
            } else if ( result === 'birthError') {
                setError('생년월일이 일치하지 않습니다.');
            } else if ( result === 'success.') {
                setError('비밀번호가 초기화되었습니다.');
                // 비밀번호 초기화 성공 시 로그인 페이지로 이동
                setTimeout(() => {
                    navigate('/login');
                }, 2000); // 2초 후에 로그인 페이지로 리디렉션
            } else {
                setError('알 수 없는 오류가 발생했습니다.');
            }
        } catch (error) {
            setError('서버와의 연결에 문제가 발생했습니다.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="find-pw-page">
            <h2>비밀번호 초기화</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="memberId">아이디</label>
                    <input
                        type="text"
                        id="memberId"
                        value={memberId}
                        onChange={(e) => setMemberId(e.target.value)}
                        placeholder="아이디를 입력해주세요"
                        required
                    />
                </div>
                <div>
                    <label htmlFor="memberBirthday">생년월일</label>
                    <input
                        type="text"
                        id="memberBirthday"
                        value={memberBirthday}
                        onChange={(e) => setMemberBirthday(e.target.value)}
                        placeholder="생년월일을 입력해주세요 (YYYY-MM-DD)"
                        required
                    />
                </div>
                <button type="submit" disabled={loading}>
                    {loading ? '로딩 중...' : '비밀번호 초기화'}
                </button>
            </form>

            {error && <p style={{ color: 'red' }}>{error}</p>}
        </div>
    );
}

export default FindPwPage;
