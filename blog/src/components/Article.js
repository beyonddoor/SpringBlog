import React, {useState} from 'react';
import {FaTrash} from 'react-icons/fa';

/**
 * to render the article title and datetime
 * @returns 
 */
const Article = ({title, date, onEdit=f=>f}) => {
    const [selected, setSelected] = useState(false);
    return (<div>
    <div>
        <span>{title}</span>
        <span>{date}</span>
        <span><button onClick={()=>setSelected(true)}><FaTrash /></button></span>
    </div>
</div>)
};

export default Article;