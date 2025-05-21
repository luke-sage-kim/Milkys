const Input = ({ type, name, value, onChange, required, label }) => {
    return (
        <label>
            {label}
            <input
                type={type}
                name={name}
                value={value}
                onChange={onChange}
                required={required}
            />
        </label>
    );
};

export default Input;
