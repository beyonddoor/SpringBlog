import CommentList from "./CommentList";

/**
 * to render article detail
 * @param {*} param0 
 * @returns 
 */
const ArticleDetail = ({ title, date, content, comments=[] }) => {
    return (
        <div>
        <h1>{title}</h1>
        <h1>{date}</h1>
        <p>{content}</p>

        <CommentList comments={comments}></CommentList>

        </div>
    );
    }

export default ArticleDetail;