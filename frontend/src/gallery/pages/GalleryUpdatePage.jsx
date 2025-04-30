// src/gallery/pages/GalleryUpdatePage.jsx
import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import useSessionData from "../../atom/components/MySession";
import Header from "../../main/components/ui/Header";
import { fetchGalleryDetail, fetchMediaFiles } from "../services/galleryService"; // 외부 서비스 파일에서 가져오기
import axios from "axios"; // axios import 추가
const GalleryUpdatePage = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const { userData, isLoggedIn } = useSessionData();
    const sessionMemberCode = userData?.memberCode;

    const [formData, setFormData] = useState({
        title: "",
        content: ""
    });

    const [mediaFiles, setMediaFiles] = useState([]);
    const [files, setFiles] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (sessionMemberCode) {
            fetchGalleryDetail(id, sessionMemberCode, setFormData, navigate);
            fetchMediaFiles(id, setMediaFiles);
            setLoading(false);
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
            await axios.put(`http://localhost:8080/gallery/v1/${id}`, {
                ...formData,
                memberCode: sessionMemberCode
            });
            alert("게시글이 수정되었습니다.");
            navigate(`/gallery-detail/${id}`);
        } catch (err) {
            console.error("게시글 수정 실패:", err);
            alert("수정에 실패했습니다.");
        }
    };

    const handleDeleteMedia = async (mediaId) => {
        if (!window.confirm("이 사진을 삭제하시겠습니까?")) return;
        try {
            await axios.delete(`http://localhost:8080/media/v1/${mediaId}`);
            setMediaFiles(prev => prev.filter(file => file.id !== mediaId));
            alert("사진이 삭제되었습니다.");
        } catch (err) {
            console.error("사진 삭제 실패:", err);
            alert("사진 삭제에 실패했습니다.");
        }
    };

    const handleFileChange = (e) => {
        const selectedFiles = e.target.files;
        setFiles(selectedFiles);
    };

    const handleUpload = async () => {
        const formData = new FormData();
        formData.append("domainType", "GALLERY");
        formData.append("parentId", id);

        Array.from(files).forEach((file) => {
            formData.append("files", file);
        });

        try {
            const response = await axios.post("http://localhost:8080/media/v1/upload", formData, {
                headers: {
                    "Content-Type": "multipart/form-data",
                },
            });

            if (response.status === 200) {
                alert("사진이 업로드되었습니다.");
                setFiles([]);
                await fetchMediaFiles(id, setMediaFiles); // 업로드 후 미디어 파일 갱신
            } else {
                alert("사진 업로드에 실패했습니다.");
            }
        } catch (err) {
            console.error("사진 업로드 실패:", err);
            alert("사진 업로드에 실패했습니다.");
        }
    };

    if (!isLoggedIn) return <div className="text-center py-10">로그인 후 이용해주세요.</div>;
    if (loading) return <div className="text-center py-10">로딩 중...</div>;

    return (
        <div className="max-w-3xl mx-auto p-6 bg-white shadow-lg rounded-lg">
            <Header isLoggedIn={isLoggedIn} nickname={userData?.memberNickname} />
            <h2 className="text-2xl font-bold text-center mt-4">갤러리 수정</h2>

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
                    <h3 className="text-lg font-semibold mb-2">등록된 사진</h3>
                    {mediaFiles.length === 0 ? (
                        <p className="text-gray-500">사진이 없습니다.</p>
                    ) : (
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            {mediaFiles.map(file => (
                                <div key={file.id} className="relative">
                                    <img
                                        src={`http://localhost:8080/media/${file.storedFilePath.split("C:/milkysDatabase/")[1]}`}
                                        alt={file.originalFileName}
                                        className="rounded-lg shadow-md object-cover w-full h-60"
                                    />
                                    <button
                                        type="button"
                                        onClick={() => handleDeleteMedia(file.id)}
                                        className="absolute top-2 right-2 bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600"
                                    >
                                        삭제
                                    </button>
                                </div>
                            ))}
                        </div>
                    )}
                </div>

                <div>
                    <label className="block font-semibold">새 사진 업로드</label>
                    <input
                        type="file"
                        multiple
                        onChange={handleFileChange}
                        className="w-full p-2 border rounded-lg"
                    />
                    <button
                        type="button"
                        onClick={handleUpload}
                        className="mt-4 bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700"
                    >
                        업로드
                    </button>
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
                        onClick={() => navigate(`/gallery-detail/${id}`)}
                        className="bg-gray-300 text-gray-700 px-6 py-2 rounded-lg hover:bg-gray-400"
                    >
                        취소
                    </button>
                </div>
            </form>
        </div>
    );
};

export default GalleryUpdatePage;
