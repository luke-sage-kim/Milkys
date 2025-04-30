import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import Header from "../../main/components/ui/Header";
import useSessionData from "../../atom/components/MySession";
import CommentSection from "../../atom/components/CommentSection";

const GalleryDetailPage = () => {
    const { id } = useParams();
    const [galleryDetail, setGalleryDetail] = useState(null);
    const [mediaFiles, setMediaFiles] = useState([]);
    const [loading, setLoading] = useState(true);
    const { isLoggedIn, userData } = useSessionData();
    const navigate = useNavigate();

    useEffect(() => {
        const fetchGalleryDetail = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/gallery/v1/${id}`);
                setGalleryDetail(response.data.resultData);
            } catch (error) {
                console.error("갤러리 상세 조회 중 오류 발생:", error);
            }
        };

        const fetchMediaFiles = async () => {
            try {
                const mediaRequest = {
                    domainType: "GALLERY",
                    parentId: id
                };
                const response = await axios.post("http://localhost:8080/media/v1/list", mediaRequest);

                if (response.status === 204 || response.data.resultData === "미디어 파일이 없습니다.") {
                    setMediaFiles([]);
                } else {
                    setMediaFiles(response.data.resultData || []);
                }
            } catch (error) {
                console.error("미디어 파일 조회 중 오류 발생:", error);
                setMediaFiles([]);
            }
        };

        const fetchData = async () => {
            await fetchGalleryDetail();
            await fetchMediaFiles();
            setLoading(false);
        };

        fetchData();
    }, [id]);

    const handleDelete = async () => {
        if (!isLoggedIn || !userData?.memberCode) {
            alert("로그인이 필요합니다.");
            return;
        }

        if (!window.confirm("정말 삭제하시겠습니까?")) return;

        try {
            await axios.delete(`http://localhost:8080/gallery/v1/${id}`);
            alert("갤러리가 삭제되었습니다.");
            navigate("/gallery");
        } catch (err) {
            console.error("갤러리 삭제 오류:", err);
            alert("삭제에 실패했습니다.");
        }
    };

    if (loading) {
        return <div className="text-center text-lg font-semibold py-10">로딩 중...</div>;
    }

    if (!galleryDetail) {
        return <div className="text-center text-lg font-semibold py-10">존재하지 않는 글입니다.</div>;
    }

    return (
        <div className="max-w-4xl mx-auto p-4">
            <Header isLoggedIn={isLoggedIn} nickname={userData.memberNickname} />
            <h1 className="text-2xl font-bold text-center my-6">갤러리 상세 페이지</h1>

            <div className="border p-4 rounded-lg shadow-md">
                <h2 className="text-xl font-bold mb-2">{galleryDetail.title}</h2>
                <p className="text-gray-600 mb-4">작성자: {galleryDetail.memberNickName}</p>
                <p className="text-gray-500 mb-4">조회수: {galleryDetail.viewCnt}</p>
                <div className="text-gray-800 whitespace-pre-wrap mb-6">{galleryDetail.content}</div>

                <div className="border-t pt-4">
                    <h3 className="text-lg font-semibold mb-2">사진</h3>
                    {mediaFiles.length === 0 ? (
                        <div className="text-center text-gray-400">
                            <p>사진이 없습니다.</p>
                            <img src="/path/to/no-image-available.png" alt="No Image" className="mx-auto w-20 h-20" />
                        </div>
                    ) : (
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            {mediaFiles.map((file) => (
                                <img
                                    key={file.id}
                                    src={`http://localhost:8080/media/${file.storedFilePath.split('C:/milkysDatabase/')[1]}`}
                                    alt={file.originalFileName}
                                    className="rounded-lg shadow-md object-cover w-full h-60"
                                />
                            ))}
                        </div>
                    )}
                </div>

                <div className="mt-6 flex justify-between">
                    <button
                        className="px-6 py-2 bg-blue-500 text-white font-semibold rounded-lg hover:bg-blue-600 transition"
                        onClick={() => navigate("/gallery")}
                    >
                        목록으로 돌아가기
                    </button>

                    {/* ✅ 삭제 버튼: 작성자만 표시 */}
                    {isLoggedIn && userData?.memberCode === galleryDetail.memberCode && (
                        <div className="flex space-x-2">
                            <button
                                onClick={() => navigate(`/gallery/update/${id}`)}
                                className="px-6 py-2 bg-green-500 text-white font-semibold rounded-lg hover:bg-green-600 transition"
                            >
                                수정
                            </button>
                            <button
                                onClick={handleDelete}
                                className="px-6 py-2 bg-red-500 text-white font-semibold rounded-lg hover:bg-red-600 transition"
                            >
                                삭제
                            </button>
                        </div>
                    )}
                </div>
            </div>

            {/* 댓글 섹션 */}
            <div className="mt-10">
                <CommentSection parentId={id} parentType="GALLERY" />
            </div>
        </div>
    );
};

export default GalleryDetailPage;
