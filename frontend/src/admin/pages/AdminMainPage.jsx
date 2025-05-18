import React from 'react';
import { Link } from 'react-router-dom';
import Header from "../../main/components/ui/Header"; // 경로는 프로젝트 구조에 맞게 조정

function AdminMainPage() {
    return (
        <div className="min-h-screen bg-gray-100 p-6">
            <Header />
            <div className="max-w-5xl mx-auto mt-10">
                <h1 className="text-3xl font-bold mb-6 text-center">👑 관리자 페이지</h1>

                {/* 회원관리 */}
                <div className="bg-white rounded-2xl shadow-md p-6 mb-8">
                    <h2 className="text-2xl font-semibold mb-4">👥 회원관리</h2>
                    <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                        <Link
                            to="/admin/member-approval"
                            className="block p-4 rounded-lg border hover:bg-gray-50 transition"
                        >
                            <h3 className="font-medium text-lg">가입요청 승인</h3>
                            <p className="text-sm text-gray-600">가입 요청 목록 확인 및 승인 처리</p>
                        </Link>

                        <Link
                            to="/admin/member-role"
                            className="block p-4 rounded-lg border hover:bg-gray-50 transition"
                        >
                            <h3 className="font-medium text-lg">회원 권한 관리</h3>
                            <p className="text-sm text-gray-600">회원 역할 변경 및 추방</p>
                        </Link>
                    </div>
                </div>

                {/* 게시글관리 */}
                <div className="bg-white rounded-2xl shadow-md p-6">
                    <h2 className="text-2xl font-semibold mb-4">📝 게시글 관리</h2>
                    <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                        <Link
                            to="/admin/board-manage"
                            className="block p-4 rounded-lg border hover:bg-gray-50 transition"
                        >
                            <h3 className="font-medium text-lg">게시글 수정 및 삭제</h3>
                            <p className="text-sm text-gray-600">모든 게시글 열람, 수정 및 삭제</p>
                        </Link>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default AdminMainPage;
