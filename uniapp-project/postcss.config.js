module.exports = {
    'plugins': {
      'autoprefixer': {
        overrideBrowserslist: [
          'Android 4.1',
          'iOS 7.1',
          'Chrome > 31',
          'not ie <= 11',  
          'ff >= 30',
          '> 1%',
          'last 2 versions', // 所有主流浏览器最近2个版本
        ],
        grid: true ,
      },
      'postcss-pxtorem': {
        rootValue: 16,
        unitPrecision: 6,
        selectorBlackList: ['.no-rem', 'no-rem'],
        propList: ['*'],
        replace: true,
        mediaQuery: true,
        minPixelValue: 2,
        exclude: /node_modules/i
      }
    }
  }