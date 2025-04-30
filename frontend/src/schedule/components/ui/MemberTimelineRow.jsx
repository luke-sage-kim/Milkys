// schedule/components/MemberTimelineRow.jsx
import React from 'react';

const MemberTimelineRow = ({ nickName, votes }) => {
    return (
        <div className="relative h-8 w-full">
            {votes.map((vote, i) => {
                const [sh, sm] = vote.scvStart.split(':').map(Number);
                const [eh, em] = vote.scvEnd.split(':').map(Number);
                const totalHours = (eh + (em > 0 ? 1 : 0)) - sh;
                const leftPercent = ((sh - 9) / 16) * 100;
                const widthPercent = (totalHours / 16) * 100;

                return (
                    <div
                        key={i}
                        className="absolute h-full bg-blue-500 text-white text-sm flex items-center justify-center rounded"
                        style={{
                            left: `${leftPercent}%`,
                            width: `${widthPercent}%`,
                        }}
                    >
                        {nickName}
                    </div>
                );
            })}
        </div>
    );
};

export default MemberTimelineRow;
