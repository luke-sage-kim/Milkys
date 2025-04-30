// schedule/components/TimeHeader.jsx
import React from 'react';

const TimeHeader = ({ hours }) => {
    return (
        <div className="flex text-sm font-medium text-gray-700 border-b pb-2">
            {hours.map(hour => (
                <div key={hour} className="w-16 text-center">
                    {hour}시
                </div>
            ))}
        </div>
    );
};

export default TimeHeader;
