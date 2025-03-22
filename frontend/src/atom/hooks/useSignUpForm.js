// src/pages/hooks/useSignUpForm.js
import { useState } from 'react';
import { validateForm } from '../utils/validateForm';  // 유효성 검사 함수

export const useSignUpForm = () => {
    const [formData, setFormData] = useState({
        username: '',
        password: '',
        confirmPassword: '',
        name: '',
        nickName: '',
        birthDate: '',
        phoneNumber: '',
    });
    const [isAgreed, setIsAgreed] = useState(false);
    const [memberSignUpCode, setMemberSignUpCode] = useState('');
    const [error, setError] = useState('');

    const handleInputChange = (event) => {
        const { name, value } = event.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handleCheckboxChange = (event) => {
        setIsAgreed(event.target.checked);
    };

    const handleMemberSignUpCodeChange = (event) => {
        setMemberSignUpCode(event.target.value);
    };

    const validate = () => {
        return validateForm(formData, setError);
    };

    return {
        formData,
        isAgreed,
        memberSignUpCode,
        error,
        handleInputChange,
        handleCheckboxChange,
        handleMemberSignUpCodeChange,
        validate,
        setError,
    };
};
