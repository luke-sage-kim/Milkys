import React, { useState } from 'react';
import './Schedule.css';
import { useNavigate } from 'react-router-dom';
import Header from "../../main/components/ui/Header";

function Schedule() {
  const [selectedDate, setSelectedDate] = useState(new Date());
  const navigate = useNavigate();

  const getFirstDayOfMonth = (year, month) => new Date(year, month, 1).getDay();
  const getDaysInMonth = (year, month) => new Date(year, month + 1, 0).getDate();

  const year = selectedDate.getFullYear();
  const month = selectedDate.getMonth();
  const firstDay = getFirstDayOfMonth(year, month);
  const daysInMonth = getDaysInMonth(year, month);
  const days = Array.from({ length: daysInMonth }, (_, i) => i + 1);

  const handlePrevMonth = () => {
    setSelectedDate(new Date(year, month - 1, 1));
  };

  const handleNextMonth = () => {
    setSelectedDate(new Date(year, month + 1, 1));
  };

  const handleDayClick = (day) => {
    const selected = new Date(year, month, day);
    const formattedDate = `${selected.getFullYear()}-${String(selected.getMonth() + 1).padStart(2, '0')}-${String(selected.getDate()).padStart(2, '0')}`;
    navigate(`/schedule-detail/${formattedDate}`);
  };

  return (
      <div className="schedule-container">
        <Header />

        <header className="calendar-header">
          <button onClick={handlePrevMonth}>{"<"}</button>
          <h3>{year}년 {month + 1}월</h3>
          <button onClick={handleNextMonth}>{">"}</button>
        </header>

        <div className="calendar-grid">
          {["일", "월", "화", "수", "목", "금", "토"].map((day) => (
              <div key={day} className="day-label">{day}</div>
          ))}
          {Array.from({ length: firstDay }, (_, i) => (
              <div key={`empty-${i}`} className="empty"></div>
          ))}
          {days.map((day) => (
              <div key={day} className="day" onClick={() => handleDayClick(day)}>
                {day}
              </div>
          ))}
        </div>
      </div>
  );
}

export default Schedule;
