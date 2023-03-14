import Comment from "./Comment"

const CommentList = ({ comments }) => (
    <div>
        {comments.map((comment, index) => (<Comment comment={comment} key={index}></Comment>))}
    </div>
)

export default CommentList