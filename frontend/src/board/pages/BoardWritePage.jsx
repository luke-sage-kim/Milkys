import React, { useState } from "react";
import axios from "axios";
import Header from "../../main/components/ui/Header";
import useSessionData from "../../atom/components/MySession";
import { useNavigate } from 'react-router-dom';

const BoardWritePage = () => {
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const [boardType, setBoardType] = useState("FREE"); // 기본값 설정
    const [error, setError] = useState("");

    const { isLoggedIn, userData } = useSessionData();
    const sessionMemberCode = userData?.memberCode;
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!title || !content || !boardType) {
            setError("모든 항목을 입력해주세요.");
            return;
        }

        const writeBoardDto = {
            memberCode: sessionMemberCode,
            title,
            content,
            boardType,
        };

        try {
            const response = await axios.post("http://localhost:8080/board/v1/write", writeBoardDto, {
                headers: {
                    "Content-Type": "application/json",
                },
            });

            if (response.status === 200) {
                alert("게시글이 작성되었습니다.");
                navigate("/board-main");
            }
        } catch (err) {
            setError("게시글 작성에 실패했습니다.");
            console.error("Board write error:", err);
        }
    };

    if (!isLoggedIn) {
        return <div className="text-center py-10 text-lg">로그인 후 이용 가능합니다.</div>;
    }

    return (
        <div className="max-w-3xl mx-auto p-6 bg-white shadow-lg rounded-lg">
            <Header isLoggedIn={isLoggedIn} nickname={userData.memberNickname} />

            <h2 className="text-2xl font-bold mb-6 text-center">게시글 작성</h2>
            <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                    <label htmlFor="title" className="block text-sm font-medium text-gray-700">제목</label>
                    <input
                        type="text"
                        id="title"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        className="mt-1 block w-full border border-gray-300 rounded-md p-2"
                        required
                    />
                </div>

                <div>
                    <label htmlFor="content" className="block text-sm font-medium text-gray-700">내용</label>
                    <textarea
                        id="content"
                        value={content}
                        onChange={(e) => setContent(e.target.value)}
                        className="mt-1 block w-full border border-gray-300 rounded-md p-2 resize-none"
                        rows={8}
                        required
                    />
                </div>

                <div>
                    <label htmlFor="boardType" className="block text-sm font-medium text-gray-700">게시판 종류</label>
                    <select
                        id="boardType"
                        value={boardType}
                        onChange={(e) => setBoardType(e.target.value)}
                        className="mt-1 block w-full border border-gray-300 rounded-md p-2"
                        required
                    >
                        <option value="FREE">자유</option>
                        <option value="NOTICE">공지</option>
                        <option value="FLASH">번개</option>
                        <option value="REVIEW">리뷰</option>
                    </select>
                </div>

                {error && <p className="text-red-500 text-sm">{error}</p>}

                <div className="flex justify-between mt-6">
                    <button
                        type="submit"
                        className="bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700 transition"
                    >
                        작성하기
                    </button>
                    <button
                        type="button"
                        onClick={() => navigate("/board-main")}
                        className="bg-gray-300 text-gray-700 px-6 py-2 rounded-lg hover:bg-gray-400 transition"
                    >
                        작성 취소
                    </button>
                </div>
            </form>
        </div>
    );
};

export default BoardWritePage;
