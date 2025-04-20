import React, { useState, useEffect } from "react";
import axios from "axios";
import { useParams, useNavigate } from "react-router-dom";
import useSessionData from "../../atom/components/MySession";
import Header from "../../main/components/ui/Header";

const BoardUpdatePage = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const { userData, isLoggedIn } = useSessionData();
    const sessionMemberCode = userData?.memberCode;

    const [formData, setFormData] = useState({
        title: "",
        content: "",
        boardType: "FREE"
    });

    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchBoardDetail = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/board/v1/${id}`);
                const data = response.data.resultData;

                if (data.memberCode !== sessionMemberCode) {
                    alert("수정 권한이 없습니다.");
                    navigate(-1);
                } else {
                    setFormData({
                        title: data.title,
                        content: data.content,
                        boardType: data.boardType
                    });
                }
            } catch (err) {
                console.error("게시글 정보를 불러오는 데 실패했습니다:", err);
                alert("게시글 정보를 불러오는 데 실패했습니다.");
                navigate(-1);
            } finally {
                setLoading(false);
            }
        };

        if (sessionMemberCode) {
            fetchBoardDetail();
        }
    }, [id, sessionMemberCode, navigate]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await axios.put(`http://localhost:8080/board/v1/${id}`, {
                ...formData,
                memberCode: sessionMemberCode
            });
            alert("게시글이 수정되었습니다.");
            navigate(`/board-detail/${id}`);
        } catch (err) {
            console.error("게시글 수정 중 오류 발생:", err);
            alert("게시글 수정에 실패했습니다.");
        }
    };

    if (!isLoggedIn) return <div className="text-center py-10">로그인 후 이용해주세요.</div>;
    if (loading) return <div className="text-center py-10">로딩 중...</div>;

    return (
        <div className="max-w-3xl mx-auto p-6 bg-white shadow-lg rounded-lg">
            <Header isLoggedIn={isLoggedIn} nickname={userData?.memberNickname} />
            <h2 className="text-2xl font-bold text-center mt-4">게시글 수정</h2>
            <form onSubmit={handleSubmit} className="mt-6 flex flex-col space-y-4">
                <div>
                    <label className="block font-semibold">제목</label>
                    <input
                        type="text"
                        name="title"
                        value={formData.title}
                        onChange={handleChange}
                        className="w-full px-4 py-2 border rounded-lg"
                        required
                    />
                </div>
                <div>
                    <label className="block font-semibold">내용</label>
                    <textarea
                        name="content"
                        value={formData.content}
                        onChange={handleChange}
                        className="w-full px-4 py-2 border rounded-lg resize-none"
                        rows={8}
                        required
                    />
                </div>
                <div>
                    <label className="block font-semibold">게시판 종류</label>
                    <select
                        name="boardType"
                        value={formData.boardType}
                        onChange={handleChange}
                        className="w-full px-4 py-2 border rounded-lg"
                        required
                    >
                        <option value="FREE">자유</option>
                        <option value="NOTICE">공지</option>
                        <option value="FLASH">번개</option>
                        <option value="REVIEW">리뷰</option>
                    </select>
                </div>
                <div className="flex justify-between mt-6">
                    <button
                        type="submit"
                        className="bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700"
                    >
                        수정하기
                    </button>
                    <button
                        type="button"
                        onClick={() => navigate(`/board-detail/${id}`)}
                        className="bg-gray-300 text-gray-700 px-6 py-2 rounded-lg hover:bg-gray-400"
                    >
                        취소
                    </button>
                </div>
            </form>
        </div>
    );
};

export default BoardUpdatePage;
