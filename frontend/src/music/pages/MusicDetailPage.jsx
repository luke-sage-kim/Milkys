import React, { useState, useEffect } from "react";
import axios from "axios";
import { useParams, useNavigate } from "react-router-dom";  // useNavigate 추가
import Header from "../../main/components/ui/Header";
import useSessionData from "../../atom/components/MySession";
import CommentSection from "../../atom/components/CommentSection";
const MusicDetailPage = () => {
    const [musicDetail, setMusicDetail] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [isLiked, setIsLiked] = useState(false);
    const [likeCount, setLikeCount] = useState(0);

    const { id } = useParams();
    const navigate = useNavigate();  // navigate 훅 선언
    const { isLoggedIn, userData } = useSessionData();
    const memberCode = userData?.memberCode;

    useEffect(() => {
        const fetchMusicDetail = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/music/v1/${id}`);
                const data = response.data.resultData;
                setMusicDetail(data);
                setLikeCount(data.like);
            } catch (err) {
                setError("음악 상세 정보를 가져오는 데 실패했습니다.");
            } finally {
                setLoading(false);
            }
        };
        fetchMusicDetail();
    }, [id]);

    useEffect(() => {
        if (isLoggedIn && memberCode) {
            axios.post("http://localhost:8080/recommend/v1/isRecommend", {
                parentId: id,
                recommendParent: "MUSIC",
                memberCode,
            }).then(response => setIsLiked(response.data.resultData))
                .catch(err => console.error("Error checking like status:", err));
        }
    }, [isLoggedIn, id]);

    const toggleLike = async () => {
        if (!isLoggedIn || !memberCode) {
            alert("로그인이 필요합니다.");
            return;
        }
        try {
            await axios.post(`http://localhost:8080/music/v1/${id}/recommend`, { memberCode });
            setIsLiked(prev => !prev);
            setLikeCount(prev => (isLiked ? prev - 1 : prev + 1));
        } catch (err) {
            console.error("Error toggling like status:", err);
        }
    };

    const handleDelete = async () => {
        if (!isLoggedIn || !memberCode) {
            alert("로그인이 필요합니다.");
            return;
        }
        try {
            await axios.delete(`http://localhost:8080/music/v1/${id}`);
            alert("음악이 삭제되었습니다.");
            navigate(`/music-main/`);  // 삭제 후 음악 리스트 페이지로 이동
        } catch (err) {
            console.error("Error deleting music:", err);
            alert("음악 삭제에 실패했습니다.");
        }
    };

    if (loading) return <div className="text-center text-lg font-semibold py-10">로딩 중...</div>;
    if (error) return <div className="text-center text-red-500">{error}</div>;
    if (!musicDetail) return <div className="text-center text-gray-500">음악 정보를 찾을 수 없습니다.</div>;

    return (
        <div className="max-w-2xl mx-auto p-6 bg-white shadow-lg rounded-lg">
            <Header isLoggedIn={isLoggedIn} nickname={userData?.memberNickname} />
            <h2 className="text-2xl font-bold text-center mt-4">{musicDetail.title}</h2>
            <p className="text-right text-sm text-gray-600 mt-2">작성자: {musicDetail.memberNickName}</p>

            <div className="mt-6 flex justify-center">
                {musicDetail.musicLink ? (
                    <iframe
                        className="w-full h-64 rounded-lg"
                        src={`https://www.youtube.com/embed/${new URL(musicDetail.musicLink).searchParams.get("v")}`}
                        title="YouTube video player"
                        frameBorder="0"
                        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                        allowFullScreen
                    ></iframe>
                ) : (
                    <p className="text-center text-gray-500">유튜브 링크가 없습니다.</p>
                )}
            </div>

            <div className="mt-6">
                <h3 className="text-lg font-semibold">내용</h3>
                <p className="text-gray-700 mt-2">{musicDetail.content}</p>
            </div>

            <div className="mt-6 flex flex-col items-center">
                <div className="w-full flex justify-between items-center">
                    <div>
                        <h3 className="text-lg font-semibold">좋아요 수</h3>
                        <p className="text-gray-700">{likeCount}개</p>
                    </div>
                    {isLoggedIn && (
                        <button
                            onClick={toggleLike}
                            className={`px-6 py-2 rounded-lg font-semibold transition ${isLiked ? 'bg-red-500 text-white' : 'bg-gray-300 text-black hover:bg-gray-400'}`}
                        >
                            {isLiked ? "❤️ 좋아요 취소" : "🤍 좋아요"}
                        </button>
                    )}
                </div>

                {isLoggedIn && musicDetail.memberCode === memberCode && (
                    <div className="flex space-x-4">
                        <button
                            onClick={() => navigate(`/music/update/${id}`)}  // 수정 버튼 클릭 시 수정 페이지로 이동
                            className="mt-6 bg-blue-500 text-white px-4 py-2 rounded-lg shadow-lg hover:bg-blue-600"
                        >
                            수정
                        </button>

                        <button
                            onClick={handleDelete}  // 삭제 버튼 클릭 시 삭제 처리
                            className="mt-6 bg-red-500 text-white px-4 py-2 rounded-lg shadow-lg hover:bg-red-600"
                        >
                            삭제
                        </button>
                    </div>
                )}
            </div>

            {/* 목록 보기 버튼 (모든 사용자에게 보임) */}
            <div className="mt-6 text-right">
                <button
                    onClick={() => navigate("/music-main")}
                    className="px-4 py-2 bg-gray-300 text-gray-800 rounded-lg hover:bg-gray-400 transition"
                >
                    목록 보기
                </button>
            </div>
            <CommentSection parentId={id} parentType="MUSIC" />
        </div>
    );
};

export default MusicDetailPage;
