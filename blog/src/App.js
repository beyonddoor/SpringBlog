import logo from './logo.svg';
import './App.css';
import ArticleList from './components/ArticleList';

function App({data}) {
  return (
    <div className="App">
      <header className="App-header">
        <ArticleList articles={data.articles}/>
      </header>
    </div>
  );
}

export default App;
