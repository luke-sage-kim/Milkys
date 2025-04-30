import React, { useState, useEffect } from "react";
import axios from "axios";
import Header from "../../main/components/ui/Header";
import useSessionData from "../../atom/components/MySession";
import { useNavigate } from "react-router-dom";

const GalleryMainPage = () => {
    const [galleryList, setGalleryList] = useState([]);
    const [loading, setLoading] = useState(true);
    const { isLoggedIn, userData } = useSessionData();
    const navigate = useNavigate();

    useEffect(() => {
        const fetchGalleryList = async () => {
            try {
                const response = await axios.get("http://localhost:8080/gallery/v1");
                setGalleryList(response.data.resultData);
            } catch (error) {
                console.error("갤러리 리스트를 가져오는 중 오류 발생:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchGalleryList();
    }, []);

    if (loading) {
        return <div className="text-center text-lg font-semibold py-10">로딩 중...</div>;
    }

    return (
        <div className="max-w-4xl mx-auto p-4">
            <Header isLoggedIn={isLoggedIn} nickname={userData.memberNickname} />
            <h1 className="text-2xl font-bold text-center my-6">갤러리 공유게시판</h1>

            <div className="overflow-x-auto">
                <table className="w-full border border-gray-200 rounded-lg shadow-md">
                    <thead className="bg-gray-100">
                    <tr className="text-gray-600 text-sm md:text-base">
                        <th className="p-3 border">순번</th>
                        <th className="p-3 border">제목</th>
                        <th className="p-3 border">내용</th>
                        <th className="p-3 border">조회수</th>
                        <th className="p-3 border">작성자</th>
                    </tr>
                    </thead>
                    <tbody>
                    {galleryList.map((gallery, index) => (
                        <tr key={gallery.id} className="text-center text-gray-800 text-sm md:text-base hover:bg-gray-50">
                            <td className="p-3 border">{index + 1}</td>
                            <td className="p-3 border">
                                <a
                                    href={`/gallery-detail/${gallery.id}`}
                                    className="text-gray-800 hover:text-blue-500 hover:underline transition"
                                >
                                    {gallery.title}
                                </a>
                            </td>
                            <td className="p-3 border">{gallery.content}</td>
                            <td className="p-3 border">{gallery.viewCnt}</td>
                            <td className="p-3 border">{gallery.memberNickName}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>

            {isLoggedIn && (
                <button
                    className="block w-full md:w-auto mx-auto mt-6 px-6 py-2 bg-green-500 text-white font-semibold rounded-lg hover:bg-green-600 transition"
                    onClick={() => navigate("/gallery-write")}
                >
                    갤러리 작성하기
                </button>
            )}
        </div>
    );
};

export default GalleryMainPage;
