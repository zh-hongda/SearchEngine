<%@page isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>基于文档的中文搜索引擎</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/boot/css/bootstrap.min.css">
    <!--    引入jquery核心js文件  -->
    <script src="${pageContext.request.contextPath}/boot/js/jquery-3.5.1.min.js"></script>
    <!--    引入bootstrap核心js文件   -->
    <script src="${pageContext.request.contextPath}/boot/js/bootstrap.min.js"></script>

    <script type="text/javascript">
        //查询
        function query() {
            //执行查询
            document.getElementById("page").value = "0";
            if(document.getElementById("page").value)
                document.getElementById("actionForm").submit();
        }
        //翻页
        function changePage(p) {
            var curpage = Number(document.getElementById("page").value);
            curpage = curpage + p;
            document.getElementById("page").value = curpage;
            judgePage(document.getElementById("page").value);

            if(document.getElementById("page").value)
                document.getElementById("actionForm").submit();
        }
        //跳页
        function skipPage() {

            document.getElementById("page").value = Number(document.getElementById("skipPage").value)-1;
            judgePage(document.getElementById("page").value);
            document.getElementById("actionForm").submit();
        }
        //检查页码是否合法
        function judgePage(p){
            if(Number(p) <= 0)
                document.getElementById("page").value = "0";
            if(Number(p) > ${pageSum}-1)
                if(${pageSum}-1 > 0)
                    document.getElementById("page").value = ${pageSum}-1;
                else{
                    document.getElementById("page").value = "0";
                }
        }

        //检查文档类型
        function checkType(p){
            if(Number(p) === 1){
                document.getElementById("docType").value = "event";
            }
            if(Number(p) === 2){
                document.getElementById("docType").value = "JIT";
            }
            document.getElementById("actionForm").submit();
        }
    </script>

<body>
    <%--标题和搜索框开始--%>
    <div class="container-fluid">
        <form method="POST" id="actionForm" action="${pageContext.request.contextPath}/NucPower/findAll">
            <%--搜索--%>
            <div class="row" style="margin-top: 20px; margin-bottom: 20px">
                <%--查询框--%>
                <div class="col-md-6">
                    <input type="text" name="queryString" id="queryString" value="${queryString}" class="form-control">
                </div>

                <%--查询按钮--%>
                <div class="col-md-2">
                    <input type="hidden" name="page" id="page" value="${pageNow}">
                    <button type="submit" onclick="query()" class="btn btn-primary">Search</button>
                </div>

                <div style="font-size: small; color: #5e5e5e">
                    <p>基于文档的中文搜索引擎<small>v0.1</small></p>
                </div>

            </div>

            <%--筛选--%>
            <div>
                <%--第一行--%>
                <div class="row">
                    <%--按照电厂筛选--%>
                    <div class="col-md-1">
                        <select class="form-control" name="powerStation" onchange="query()">
                            <option value="*" <c:if test="${powerStation == '*'}">selected</c:if>>电厂不限</option>
                            <option value="秦一厂" <c:if test="${powerStation == '秦一厂'}">selected</c:if>>秦一厂</option>
                            <option value="秦二厂" <c:if test="${powerStation == '秦二厂'}">selected</c:if>>秦二厂</option>
                            <option value="秦三厂" <c:if test="${powerStation == '秦三厂'}">selected</c:if>>秦三厂</option>
                            <option value="方家山" <c:if test="${powerStation == '方家山'}">selected</c:if>>方家山</option>
                        </select>
                    </div>

                    <%--按照机组筛选--%>
                    <div class="col-md-1">
                        <select class="form-control" name="unit" onchange="query()">
                            <option value="*" <c:if test="${unit == '*'}">selected</c:if>>机组不限</option>
                            <option value="一号机组" <c:if test="${unit == '一号机组'}">selected</c:if>>一号机组</option>
                            <option value="二号机组" <c:if test="${unit == '二号机组'}">selected</c:if>>二号机组</option>
                        </select>
                    </div>
                </div>

                <%--第二行   按文档类型筛选--%>
                <div class="row">
                    <input type="hidden" name="docType" id="docType" value="${docType}">
                    <ul class="nav nav-tabs">
                        <li class="nav-item  <c:if test="${docType != 'JIT'}">active</c:if>" onclick="checkType(1)">
                            <a href="#event" data-toggle="tab">事件</a>
                        </li>
                        <li class="nav-item <c:if test="${docType == 'JIT'}">active</c:if>" onclick="checkType(2)">
                            <a href="#JIT" data-toggle="tab">JIT</a>
                        </li>
                    </ul>
                </div>
            </div>
        </form>
    </div>
    <%--标题和搜索框结束--%>

    <%--符合查询条件的总项数开始--%>
    <div class="container-fluid" style="margin-top: 5px; margin-bottom: 5px">

            <p>共${sumNumb}项符合查询条件。</p>

    </div>
    <%--符合查询条件的总项数结束--%>


    <!-- Tab panes -->
    <div class="tab-content">
        <div class="tab-pane <c:if test="${docType != 'JIT'}">active</c:if>" id="event" role="tabpanel" aria-labelledby="profile-tab">
            <%--查询结果列表开始--%>
            <div class="container-fluid">
                <div class="row">
                    <div class="col-md-10">
                        <ul class="list-unstyled">
                            <li>
                                <c:forEach var="item" items="${events}" varStatus="status">

                                    <div style="border:1px solid #CCC"></div>
                                    <p style="font-size: medium" data-toggle="collapse" data-target="#${status.count}">
                                        &nbsp;<small>${status.count}</small> &nbsp; ${item.eventName}<span class="caret"></span>
                                    </p>
                                    <p style="border-top:1px silver dashed;" class="collapse" id="${status.count}">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${item.overView}</p>

                                </c:forEach>
                            </li>
                        </ul>
                    </div>

                    <div class="col-md-2"></div>
                </div>
            </div>
            <%--查询结果列表结束--%>
        </div>
        <div class="tab-pane <c:if test="${docType == 'JIT'}">active</c:if>" id="JIT" role="tabpanel" aria-labelledby="home-tab">
            <%--查询结果列表开始--%>
            <div class="container-fluid">
                <div class="row">
                    <div class="col-md-10">
                        <ul class="list-unstyled">
                            <li>
                                <c:forEach var="item" items="${fileWords}" varStatus="status">

                                    <div style="border:1px solid #CCC"></div>
                                    <p style="font-size: medium" data-toggle="collapse" data-target="#${status.count}">
                                        &nbsp;<small>${status.count}</small> &nbsp; ${item.file}<span class="caret"></span>
                                    </p>
                                    <p style="border-top:1px silver dashed;" class="collapse" id="${status.count}">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${item.text}</p>

                                </c:forEach>
                            </li>
                        </ul>
                    </div>

                    <div class="col-md-2"></div>
                </div>
            </div>
            <%--查询结果列表结束--%>
        </div>
    </div>

</body>
</html>

<%--分页开始--%>
<div class="container-fluid">

    <div class="col-md-2 col-md-offset-5">
        <label>
            <input type="text" name="skipPage" id="skipPage" style="width: 50px" class="form-control">
        </label>
        <button type="submit" onclick="skipPage()" class="btn btn-primary">跳转</button>
    </div>

    <div class="col-md-3">
        <a href="javascript:changePage(-1)" class="btn btn-primary">Previous</a>

        <a class="btn"><small>第</small><span style="font-size: x-large">${pageNow+1}</span>/${pageSum}<small>页</small></a>

        <a href="javascript:changePage(1)" class="btn btn-primary">Next</a>
    </div>

</div>
<%--分页结束--%>
