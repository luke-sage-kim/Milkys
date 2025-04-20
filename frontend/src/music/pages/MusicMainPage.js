import React, { useState, useEffect } from "react";
import axios from "axios";
import Header from "../../main/components/ui/Header";
import useSessionData from "../../atom/components/MySession";
import { useNavigate } from "react-router-dom";

const MusicMainPage = () => {
    const [musicList, setMusicList] = useState([]);
    const [loading, setLoading] = useState(true);
    const { isLoggedIn, userData } = useSessionData();
    const navigate = useNavigate();

    useEffect(() => {
        const fetchMusicList = async () => {
            try {
                const response = await axios.get("http://localhost:8080/music/v1");
                setMusicList(response.data.resultData);
            } catch (error) {
                console.error("음악 리스트를 가져오는 중 오류 발생:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchMusicList();
    }, []);

    if (loading) {
        return <div className="text-center text-lg font-semibold py-10">로딩 중...</div>;
    }

    return (
        <div className="max-w-4xl mx-auto p-4">
            <Header isLoggedIn={isLoggedIn} nickname={userData.memberNickname} />
            <h1 className="text-2xl font-bold text-center my-6">음악 공유게시판</h1>

            <div className="overflow-x-auto">
                <table className="w-full border border-gray-200 rounded-lg shadow-md">
                    <thead className="bg-gray-100">
                    <tr className="text-gray-600 text-sm md:text-base">
                        <th className="p-3 border">순번</th>
                        <th className="p-3 border">제목</th>
                        <th className="p-3 border">좋아요</th>
                        <th className="p-3 border">상태</th>
                        <th className="p-3 border">작성자</th>
                    </tr>
                    </thead>
                    <tbody>
                    {musicList.map((music, index) => (
                        <tr key={music.id} className="text-center text-gray-800 text-sm md:text-base hover:bg-gray-50">
                            <td className="p-3 border">{index + 1}</td>
                            <td className="p-3 border">
                                <a
                                    href={`/music-detail/${music.id}`}
                                    className="text-gray-800 hover:text-blue-500 hover:underline transition"
                                >
                                    {music.title}
                                </a>
                            </td>

                            <td className="p-3 border">{music.like}</td>
                            <td className="p-3 border">{music.status}</td>
                            <td className="p-3 border">{music.memberNickName}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>

            {isLoggedIn && (
                <button
                    className="block w-full md:w-auto mx-auto mt-6 px-6 py-2 bg-green-500 text-white font-semibold rounded-lg hover:bg-green-600 transition"
                    onClick={() => navigate("/music-write")}
                >
                    음악 작성하기
                </button>
            )}
        </div>
    );
};

export default MusicMainPage;
