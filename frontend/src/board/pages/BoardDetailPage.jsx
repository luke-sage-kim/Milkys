import React, { useState, useEffect } from "react";
import axios from "axios";
import { useParams, useNavigate } from "react-router-dom";
import Header from "../../main/components/ui/Header";
import useSessionData from "../../atom/components/MySession";
import CommentSection from "../../atom/components/CommentSection";

const BoardDetailPage = () => {
    const [boardDetail, setBoardDetail] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const { id } = useParams();
    const navigate = useNavigate();
    const { isLoggedIn, userData } = useSessionData();
    const sessionMemberCode = userData?.memberCode; // 세션에서 가져온 memberCode

    // 게시판 타입을 한글로 변환
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

    // 게시글 상세 조회
    useEffect(() => {
        const fetchBoardDetail = async () => {
            console.log("🔥 게시글 요청 시작");
            try {
                const response = await axios.get(`http://localhost:8080/board/v1/${id}`);
                setBoardDetail(response.data.resultData);
            } catch (err) {
                setError("게시글 정보를 가져오는 데 실패했습니다.");
            } finally {
                setLoading(false);
            }
        };
        fetchBoardDetail();
    }, [id]);

    // 삭제 처리
    const handleDelete = async () => {
        if (!isLoggedIn || !sessionMemberCode) {
            alert("로그인이 필요합니다.");
            return;
        }

        if (!window.confirm("정말 삭제하시겠습니까?")) return;

        try {
            await axios.delete(`http://localhost:8080/board/v1/${id}`);
            alert("게시글이 삭제되었습니다.");
            navigate("/board-main");
        } catch (err) {
            console.error("게시글 삭제 오류:", err);
            alert("게시글 삭제에 실패했습니다.");
        }
    };

    // 로딩 및 에러 처리
    if (loading) return <div className="text-center text-lg font-semibold py-10">로딩 중...</div>;
    if (error) return <div className="text-center text-red-500">{error}</div>;
    if (!boardDetail) return <div className="text-center text-gray-500">게시글 정보를 찾을 수 없습니다.</div>;

    return (
        <div className="max-w-3xl mx-auto p-6 bg-white shadow-lg rounded-lg">
            <Header isLoggedIn={isLoggedIn} nickname={userData?.memberNickname} />

            {/* 제목 */}
            <h2 className="text-3xl font-bold text-center mt-4">{boardDetail.title}</h2>

            {/* 메타 정보 */}
            <div className="mt-4 flex justify-between text-sm text-gray-600">
                <span>작성자: {boardDetail.memberNickName}</span>
                <span>조회수: {boardDetail.viewCnt}</span>
                <span>유형: {getBoardTypeLabel(boardDetail.boardType)}</span>
            </div>

            {/* 내용 */}
            <div className="mt-6">
                <h3 className="text-lg font-semibold">내용</h3>
                <p className="text-gray-700 mt-2 whitespace-pre-line">{boardDetail.content}</p>
            </div>

            {/* 수정/삭제 버튼 (작성자만) */}
            {isLoggedIn && boardDetail.memberCode === sessionMemberCode && (
                <div className="flex space-x-4 mt-6 justify-end">
                    <button
                        onClick={() => navigate(`/board/update/${id}`)}
                        className="bg-blue-500 text-white px-4 py-2 rounded-lg shadow-lg hover:bg-blue-600"
                    >
                        수정
                    </button>
                    <button
                        onClick={handleDelete}
                        className="bg-red-500 text-white px-4 py-2 rounded-lg shadow-lg hover:bg-red-600"
                    >
                        삭제
                    </button>
                </div>
            )}

            {/* 목록 보기 버튼 */}
            <div className="mt-6 text-right">
                <button
                    onClick={() => navigate("/board-main")}
                    className="px-4 py-2 bg-gray-300 text-gray-800 rounded-lg hover:bg-gray-400 transition"
                >
                    목록 보기
                </button>
            </div>

            {/* 댓글 */}
            <CommentSection parentId={id} parentType="BOARD" />
        </div>
    );
};

export default BoardDetailPage;
