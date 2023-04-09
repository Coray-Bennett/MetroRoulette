
const LineSelect = ({source, desc, selected, onClick}) => {

    return (
        <>
        <img width="50px" src={source} alt={desc} onClick={onClick}
            style={{opacity: selected ? 1 : 0.5}}></img>
        </>
    )
}

export default LineSelect;