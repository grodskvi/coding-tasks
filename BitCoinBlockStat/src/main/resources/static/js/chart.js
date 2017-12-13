
  function createBlockDataProvider() {
    var blockDataProvider = new Object()

    blockDataProvider.fetchData = function (url, parameters, onDataReceivedCallbacks) {
      var xhr = new XMLHttpRequest();

        var fullUrl = buildUrl(url, parameters)
        console.log("url: " + fullUrl)
      xhr.open('GET', fullUrl);
      xhr.onload = function() {
        if (xhr.status === 200) {
          var data = JSON.parse(xhr.responseText)
          for(var i = 0; i < onDataReceivedCallbacks.length; i++) {
            onDataReceivedCallbacks[i](data)
          }
        } else {
          console.log('Request failed.  Returned status of ' + xhr.status + "; " + xhr);
        }
      };
      xhr.send();
    }

    return blockDataProvider
  }

  function buildUrl(url, parameters) {
    var query = [];
    for (let param in parameters) {
      var name = encodeURIComponent(param)
      var value = parameters[param]
      query.push(name + '=' + encodeURIComponent(value));
    }
    return url + "?" + query.join('&');

  }

  function initLastBlocksChart(chartContainer, size) {
    var title = {
        text: "Last " + size + " Blocks Generation Speed"
    }
    var initialData = [
        {
            type: "line",
            dataPoints: [ ]
        }
    ]

    var chart = new CanvasJS.Chart(chartContainer, {
      title: title,
      data: initialData
    });

    chart.updateData = function(lastBlocks) {
      var dataPoints = chart.data[0].dataPoints
      for (var i = lastBlocks.length - 1; i >= 0; i--) {
        var block = lastBlocks[i]
        dataPoints.push({x: block['height'], y: block['generationSpeed']})
      }
      while (chart.size < dataPoints.length) {
        dataPoints.shift()
      }
      chart.data[0].set("dataPoints", dataPoints)
    }
    chart.size = size
    chart.render()

    return chart
  }

  function renderInitialDataSet(chart, dataProvider) {
      dataProvider.fetchData('statistics/lastblocks', {limit: chart.size}, [chart.updateData])
  }

  function scheduleChartUpdates(chart, dataProvider, interval) {
    var updateChart = function() {
        var dataPoints = chart.data[0].dataPoints
        var lastBlockHeight = dataPoints.length > 0 ? dataPoints[dataPoints.length - 1].x : 0
        var parameters = {limit: chart.size, lastHeight: lastBlockHeight}

        dataProvider.fetchData('statistics/lastblocks', parameters, [chart.updateData])
    }

    setInterval(updateChart, toMilliSeconds(interval));
  }

  function toMilliSeconds(interval) {
    switch(interval['unit']) {
        case 'second': return interval['value'] * 1000
        case 'minute': return interval['value'] * 1000 * 60
    }
  }