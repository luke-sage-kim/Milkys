import React, { useState, useEffect } from "react";
import axios from "axios";
import Header from "../../main/components/ui/Header";
import useSessionData from "../../atom/components/MySession";
import { useNavigate } from "react-router-dom";

const BoardMainPage = () => {
    const [boardList, setBoardList] = useState([]);
    const [loading, setLoading] = useState(true);
    const { isLoggedIn, userData } = useSessionData();
    const navigate = useNavigate();

    useEffect(() => {
        const fetchBoardList = async () => {
            try {
                const response = await axios.get("http://localhost:8080/board/v1");
                setBoardList(response.data.resultData);
            } catch (error) {
                console.error("게시글을 불러오는 중 오류 발생:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchBoardList();
    }, []);

    const getBoardTypeLabel = (type) => {
        switch (type) {
            case "FREE":
                return "자유";
            case "NOTICE":
                return "공지";
            case "FLASH":
                return "번개";
            case "REVIEW":
                return "리뷰";
            default:
                return type;
        }
    };

    if (loading) {
        return <div className="text-center text-lg font-semibold py-10">로딩 중...</div>;
    }

    return (
        <div className="max-w-4xl mx-auto p-4">
            <Header isLoggedIn={isLoggedIn} nickname={userData.memberNickname} />
            <h1 className="text-2xl font-bold text-center my-6">📋 게시판 메인</h1>

            <div className="overflow-x-auto">
                <table className="w-full border border-gray-200 rounded-lg shadow-md">
                    <thead className="bg-gray-100">
                    <tr className="text-gray-600 text-sm md:text-base">
                        <th className="p-3 border">번호</th>
                        <th className="p-3 border">제목</th>
                        <th className="p-3 border">게시판 유형</th>
                        <th className="p-3 border">조회수</th>
                        <th className="p-3 border">작성자</th>
                    </tr>
                    </thead>
                    <tbody>
                    {boardList.map((board, index) => (
                        <tr key={board.id} className="text-center text-gray-800 text-sm md:text-base hover:bg-gray-50">
                            <td className="p-3 border">{index + 1}</td>
                            <td className="p-3 border">
                                <a
                                    href={`/board-detail/${board.id}`}
                                    className="text-blue-600 hover:underline transition"
                                >
                                    {board.title}
                                </a>
                            </td>
                            <td className="p-3 border">{getBoardTypeLabel(board.boardType)}</td>
                            <td className="p-3 border">{board.viewCnt}</td>
                            <td className="p-3 border">{board.memberNickName}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>

            {isLoggedIn && (
                <button
                    className="block w-full md:w-auto mx-auto mt-6 px-6 py-2 bg-blue-500 text-white font-semibold rounded-lg hover:bg-blue-600 transition"
                    onClick={() => navigate("/board-write")}
                >
                    게시글 작성하기
                </button>
            )}
        </div>
    );
};

export default BoardMainPage;
