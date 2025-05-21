// src/pages/SignUpPage.jsx
import React, { useState } from 'react';
import './SignUpPage.css';
import { useNavigate } from 'react-router-dom';
import Input from '../../atom/components/Input';
import { useSignUpForm } from "../../atom/hooks/useSignUpForm";
import { signUp } from '../services/signUp';

// 중복확인 API 함수 추가 (fetch or axios 사용 예)
async function checkDuplicateMemberId(memberId) {
    try {
        const response = await fetch('http://localhost:8080/member/v1/duplicate', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ memberId }),
        });
        const data = await response.json();
        return data;
    } catch (error) {
        return { error: '서버 통신 실패' };
    }
}

function SignUpPage() {
    const navigate = useNavigate();
    const {
        formData,
        isAgreed,
        memberSignUpCode,
        error,
        handleCheckboxChange,
        handleMemberSignUpCodeChange,
        handleInputChange,
        validate,
        setError
    } = useSignUpForm();

    // 중복 확인 상태 관리
    const [idChecked, setIdChecked] = useState(false);
    const [idCheckMessage, setIdCheckMessage] = useState('');

    // 아이디 중복 확인 핸들러
    const handleCheckDuplicate = async () => {
        if (!formData.username) {
            alert('아이디를 입력해주세요.');
            return;
        }
        const result = await checkDuplicateMemberId(formData.username);
        if (result?.resultData?.resultData === '중복없음') {
            setIdChecked(true);
            setIdCheckMessage('사용 가능한 아이디입니다.');
        } else if (result?.resultData?.resultData === '중복') {
            setIdChecked(false);
            setIdCheckMessage('이미 사용 중인 아이디입니다.');
        } else {
            setIdChecked(false);
            setIdCheckMessage('중복확인에 실패했습니다. 다시 시도해주세요.');
        }
    };

    const handleCancelClick = () => {
        navigate('/login'); // 로그인 페이지로 이동
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        // 아이디 중복 확인 했는지 체크
        if (!idChecked) {
            alert('아이디 중복 확인을 해주세요.');
            return;
        }

        if (memberSignUpCode !== 'bass0618') {
            alert('유효한 멤버가입코드를 입력해주세요.');
            return;
        }

        if (!isAgreed) {
            alert('개인정보 수집에 동의해주세요.');
            return;
        }

        if (!validate()) {
            return;
        }

        const { success, data, error: apiError } = await signUp(formData);

        if (!success) {
            setError(apiError);
            return;
        }

        alert('회원가입이 완료되었습니다.');
        navigate('/login');
    };

    return (
        <div className="signup-page">
            <h2>회원가입</h2>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            <form className="signup-form" onSubmit={handleSubmit}>
                <Input
                    type="text"
                    name="username"
                    label="아이디:"
                    value={formData.username}
                    onChange={(e) => {
                        handleInputChange(e);
                        setIdChecked(false);          // 아이디 변경 시 중복 확인 초기화
                        setIdCheckMessage('');
                    }}
                    required
                />
                <button
                    type="button"
                    onClick={handleCheckDuplicate}
                    style={{ marginBottom: '10px' }}
                >
                    중복확인
                </button>
                {idCheckMessage && (
                    <p style={{ color: idChecked ? 'green' : 'red' }}>{idCheckMessage}</p>
                )}

                <Input
                    type="password"
                    name="password"
                    label="비밀번호:"
                    value={formData.password}
                    onChange={handleInputChange}
                    required
                />
                <Input
                    type="password"
                    name="confirmPassword"
                    label="비밀번호 확인:"
                    value={formData.confirmPassword}
                    onChange={handleInputChange}
                    required
                />
                <Input
                    type="text"
                    name="name"
                    label="이름:"
                    value={formData.name}
                    onChange={handleInputChange}
                    required
                />
                <Input
                    type="text"
                    name="nickName"
                    label="닉네임:"
                    value={formData.nickName}
                    onChange={handleInputChange}
                    required
                />
                <Input
                    type="text"
                    name="birthDate"
                    label="생년월일:"
                    value={formData.birthDate}
                    onChange={handleInputChange}
                    required
                />
                <Input
                    type="text"
                    name="phoneNumber"
                    label="전화번호:"
                    value={formData.phoneNumber}
                    onChange={handleInputChange}
                    required
                />

                <label className="agreement-checkbox">
                    <input
                        type="checkbox"
                        name="agreement"
                        checked={isAgreed}
                        onChange={handleCheckboxChange}
                        required
                    />
                    개인정보 수집에 동의합니다.
                </label>

                <Input
                    type="text"
                    name="memberSignUpCode"
                    label="멤버가입코드:"
                    value={memberSignUpCode}
                    onChange={handleMemberSignUpCodeChange}
                    required
                />

                <div className="button-group">
                    <button type="submit" disabled={!idChecked}>회원가입</button>
                    <button type="button" onClick={handleCancelClick}>취소</button>
                </div>
            </form>
        </div>
    );
}

export default SignUpPage;
