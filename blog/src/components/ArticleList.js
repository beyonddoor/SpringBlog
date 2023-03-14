import Article from "./Article";
import ArticleDetail from "./ArticleDetail";

/**
 * article list component
 *  to render the list of articles
 * @param {*} param0 
 * @returns 
 */

const ArticleList = ({articles}=[])=>(
    <div>
    {
        articles.map((article,_) => (
        !article.selected ? (<Article {...article} key={article.id}></Article>) :
        (<ArticleDetail {...article} key={article.id}></ArticleDetail>)
    ))}
    </div>
)
export default ArticleList;