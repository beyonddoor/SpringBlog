/**
 * to render the article title and content
 */

const ArticleEdit = ({ title, content, onSubmit=f=>f }) => (
    <div>
        <form action="post" onSubmit={onSubmit}>
        <input type="text" defaultValue={title} />
        <input type="text" defaultValue={content} />
        </form>
    </div>
)

export default ArticleEdit;
