import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Header from '../../main/components/ui/Header';

const ROLE_OPTIONS = ["USER", "LOCAMANAGER", "LEADER", "UNAPPROVAL"];

const AdminMemberPage = () => {
    const [members, setMembers] = useState([]);
    const [editedRoles, setEditedRoles] = useState({});

    const currentUserAuth = "ADMIN"; // TODO: 세션에서 가져올 것

    useEffect(() => {
        axios.get('http://localhost:8080/member/v1/memberList')
            .then(response => {
                const userOnly = response.data.resultData.filter(
                    member => member.memberAuth !== 'ADMIN' && member.memberAuth !== 'UNAPPROVAL'
                );
                setMembers(userOnly);

                const initialRoles = {};
                userOnly.forEach(m => {
                    initialRoles[m.memberCode] = m.memberAuth;
                });
                setEditedRoles(initialRoles);
            })
            .catch(error => {
                console.error('회원 목록 불러오기 실패:', error);
            });
    }, []);


    const handleRoleChange = (code, newAuth) => {
        setEditedRoles(prev => ({
            ...prev,
            [code]: newAuth
        }));
    };
    const handleKickMember = (memberCode) => {
        if (window.confirm("정말로 이 회원을 추방하시겠습니까?")) {
            axios.delete(`http://localhost:8080/member/v1/memberDelete?memberCode=${memberCode}`)
                .then(() => {
                    alert("회원이 추방되었습니다.");
                    setMembers(prev => prev.filter(m => m.memberCode !== memberCode));
                })
                .catch(error => {
                    console.error('회원 추방 실패:', error);
                    alert("회원 추방에 실패했습니다.");
                });
        }
    };

    const handleRoleSubmit = (memberCode) => {
        const payload = {
            memberCode: memberCode,
            memberAuth: currentUserAuth,
            targetAuth: editedRoles[memberCode]
        };

        axios.put('http://localhost:8080/member/v1/auth', payload)
            .then(() => {
                alert("권한이 성공적으로 변경되었습니다.");
                setMembers(prev =>
                    prev.map(m => m.memberCode === memberCode ? { ...m, memberAuth: payload.targetAuth } : m)
                );
            })
            .catch(error => {
                console.error('권한 변경 실패:', error);
                alert("권한 변경에 실패했습니다.");
            });
    };

    return (
        <div className="min-h-screen bg-gray-100 p-6">
            <Header />
            <div className="max-w-6xl mx-auto mt-10">
                <h1 className="text-3xl font-bold mb-6 text-center">👥 회원 권한 관리</h1>
                <div className="bg-white shadow-md rounded-2xl overflow-x-auto">
                    <table className="min-w-full text-sm text-left text-gray-700">
                        <thead className="text-xs uppercase bg-gray-200 text-gray-700">
                        <tr>
                            <th className="px-6 py-3">아이디</th>
                            <th className="px-6 py-3">이름</th>
                            <th className="px-6 py-3">닉네임</th>
                            <th className="px-6 py-3">생년월일</th>
                            <th className="px-6 py-3">전화번호</th>
                            <th className="px-6 py-3">현재 권한</th>
                            <th className="px-6 py-3">권한 변경</th>
                        </tr>
                        </thead>
                        <tbody>
                        {members.map(member => (
                            <tr key={member.memberId} className="border-b hover:bg-gray-50">
                                <td className="px-6 py-4">{member.memberId}</td>
                                <td className="px-6 py-4">{member.memberName}</td>
                                <td className="px-6 py-4">{member.memberNickname}</td>
                                <td className="px-6 py-4">{member.memberBirthday}</td>
                                <td className="px-6 py-4">{member.memberPhoneNumber}</td>
                                <td className="px-6 py-4">{member.memberAuth}</td>
                                <td className="px-6 py-4 flex items-center gap-2">
                                    <select
                                        value={editedRoles[member.memberCode] || member.memberAuth}
                                        onChange={(e) => handleRoleChange(member.memberCode, e.target.value)}
                                        className="border rounded px-2 py-1 text-sm"
                                    >
                                        {ROLE_OPTIONS.map(role => (
                                            <option key={role} value={role}>{role}</option>
                                        ))}
                                    </select>
                                    <button
                                        onClick={() => handleRoleSubmit(member.memberCode)}
                                        className="ml-2 bg-blue-600 hover:bg-blue-700 text-white px-3 py-1 rounded text-xs"
                                    >
                                        권한수정
                                    </button>
                                    <button
                                        onClick={() => handleKickMember(member.memberCode)}
                                        className="bg-red-600 hover:bg-red-700 text-white px-3 py-1 rounded text-xs"
                                    >
                                        회원추방
                                    </button>
                                </td>
                            </tr>
                        ))}
                        {members.length === 0 && (
                            <tr>
                                <td colSpan="7" className="px-6 py-4 text-center text-gray-500">
                                    표시할 회원이 없습니다.
                                </td>
                            </tr>
                        )}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
};

export default AdminMemberPage;
