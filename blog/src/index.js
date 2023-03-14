import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';

const root = ReactDOM.createRoot(document.getElementById('root'));
const article1 = {id: '1', title:'t1', date:'yestoday', content:'content', comments :[{
  id:'1',
  username: 'user1',
  comment: 'comment1'
},
{
  id:'2',
  username: 'user2',
  comment: 'comment2'
}
]}
article1.onEdit = function() {
  article1.selected=!article1.selected
}
const data = [article1, {id:'2', title:'t2', date:'today', content:'c2'}]
root.render(
  <React.StrictMode>
    <App data={{articles: data}}/>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
