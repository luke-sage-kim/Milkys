import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import findIdServices from '../services/findIdServices'; // 서비스 파일 import

function FindIdPage() {
    const navigate = useNavigate(); // useNavigate 훅을 사용하여 이동

    const [name, setName] = useState('');
    const [phone, setPhone] = useState('');
    const [memberId, setMemberId] = useState(null);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    // 아이디 찾기 폼 제출 처리 함수
    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!name || !phone) {
            setError('이름과 전화번호를 모두 입력해주세요.');
            return;
        }

        setLoading(true);
        try {
            // findId 서비스 호출
            const responseData = await findIdServices.findId(name, phone);

            if (responseData && responseData.resultData && responseData.resultData.resultData) {
                const foundMemberId = responseData.resultData.resultData;  // resultData 안의 resultData 값
                setMemberId(foundMemberId);  // 아이디 설정
                setError(''); // 에러 초기화
            } else {
                setError('아이디를 찾을 수 없습니다.');
            }
        } catch (error) {
            setError(error.message); // 서비스에서 던진 에러 메시지 처리
        } finally {
            setLoading(false);
        }
    };

    // 로그인 페이지로 이동하는 함수
    const handleLoginRedirect = () => {
        navigate('/login');
    };

    return (
        <div className="find-id-page">
            <h2>아이디 찾기</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="name">이름</label>
                    <input
                        type="text"
                        id="name"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        placeholder="이름을 입력해주세요"
                        required
                    />
                </div>
                <div>
                    <label htmlFor="phone">전화번호</label>
                    <input
                        type="text"
                        id="phone"
                        value={phone}
                        onChange={(e) => setPhone(e.target.value)}
                        placeholder="전화번호를 입력해주세요"
                        required
                    />
                </div>
                <button type="submit" disabled={loading}>
                    {loading ? '로딩 중...' : '아이디 찾기'}
                </button>
            </form>

            {error && <p style={{ color: 'red' }}>{error}</p>}

            {memberId && (
                <div>
                    <p>찾은 아이디: {memberId}</p>
                    <button onClick={handleLoginRedirect}>로그인하기</button>
                </div>
            )}
        </div>
    );
}

export default FindIdPage;
