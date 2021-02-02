exec = require('cordova/exec');

module.exports = {
  hasPrinter: function (resolve, reject) {
    exec(resolve, reject, "Printer", "hasPrinter", []);
  },
  getServiceVersion: function (resolve, reject) {
    exec(resolve, reject, "Printer", "getServiceVersion", []);
  },
  printerInit: function (resolve, reject) {
    exec(resolve, reject, "Printer", "printerInit", []);
  },
  printerSelfChecking: function (resolve, reject) {
    exec(resolve, reject, "Printer", "printerSelfChecking", []);
  },
  getPrinterSerialNo: function (resolve, reject) {
    exec(resolve, reject, "Printer", "getPrinterSerialNo", []);
  },
  getPrinterVersion: function (resolve, reject) {
    exec(resolve, reject, "Printer", "getPrinterVersion", []);
  },
  getPrinterModal: function (resolve, reject) {
    exec(resolve, reject, "Printer", "getPrinterModal", []);
  },
  getPrintedLength: function (resolve, reject) {
    exec(resolve, reject, "Printer", "getPrintedLength", []);
  },
  lineWrap: function (count, resolve, reject) {
    exec(resolve, reject, "Printer", "lineWrap", [count]);
  },
  sendRAWData: function (base64Data, resolve, reject) {
    exec(resolve, reject, "Printer", "sendRAWData", [base64Data]);
  },
  setAlignment: function (alignment, resolve, reject) {
    exec(resolve, reject, "Printer", "setAlignment", [alignment]);
  },
  setFontName: function (typeface, resolve, reject) {
    exec(resolve, reject, "Printer", "setFontName", [typeface]);
  },
  setFontSize: function (text, resolve, reject) {
    exec(resolve, reject, "Printer", "setFontSize", [text]);
  },
  printText: function (fontSize, resolve, reject) {
    exec(resolve, reject, "Printer", "printText", [fontSize]);
  },
  printTextWithFont: function (text, typeface, fontSize, resolve, reject) {
    exec(resolve, reject, "Printer", "printTextWithFont", [text, typeface, fontSize]);
  },
  printColumnsText: function (colsTextArr, colsWidthArr, colsAlign, resolve, reject) {
    exec(resolve, reject, "Printer", "printColumnsText", [colsTextArr, colsWidthArr, colsAlign]);
  },
  printBitmap: function (base64Data, width, height, resolve, reject) {
    exec(resolve, reject, "Printer", "printBitmap", [base64Data, width, height]);
  },
  printBarCode: function (barCodeData, symbology, width, height, textPosition, resolve, reject) {
    exec(resolve, reject, "Printer", "printBarCode", [barCodeData, symbology, width, height, textPosition]);
  },
  printQRCode: function (qrCodeData, moduleSize, errorLevel, resolve, reject) {
    exec(resolve, reject, "Printer", "printQRCode", [qrCodeData, moduleSize, errorLevel]);
  },
  printOriginalText: function (text, resolve, reject) {
    exec(resolve, reject, "Printer", "printOriginalText", [text]);
  },
  commitPrinterBuffer: function (resolve, reject) {
    exec(resolve, reject, "Printer", "commitPrinterBuffer", []);
  },
  enterPrinterBuffer: function (clean, resolve, reject) {
    exec(resolve, reject, "Printer", "enterPrinterBuffer", [clean]);
  },
  exitPrinterBuffer: function (commit, resolve, reject) {
    exec(resolve, reject, "Printer", "exitPrinterBuffer", [commit]);
  },
  tax: function (base64Data, resolve, reject) {
    exec(resolve, reject, "Printer", "tax", [base64Data]);
  },
  getPrinterFactory: function (resolve, reject) {
    exec(resolve, reject, "Printer", "getPrinterFactory", []);
  },
  clearBuffer: function (resolve, reject) {
    exec(resolve, reject, "Printer", "clearBuffer", []);
  },
  commitPrinterBufferWithCallback: function (resolve, reject) {
    exec(resolve, reject, "Printer", "commitPrinterBufferWithCallback", []);
  },
  exitPrinterBufferWithCallback: function (commit, resolve, reject) {
    exec(resolve, reject, "Printer", "exitPrinterBufferWithCallback", [commit]);
  },
  printColumnsString: function (colsTextArr, colsWidthArr, colsAlign, resolve, reject) {
    exec(resolve, reject, "Printer", "printColumnsString", [colsTextArr, colsWidthArr, colsAlign]);
  },
  updatePrinterState: function (resolve, reject) {
    exec(resolve, reject, "Printer", "updatePrinterState", []);
  },
  printBitmapCustom: function (base64Data, width, height, type, resolve, reject) {
    exec(resolve, reject, "Printer", "printBitmapCustom", [base64Data, width, height, type]);
  },
  getForcedDouble: function (resolve, reject) {
    exec(resolve, reject, "Printer", "getForcedDouble", []);
  },
  isForcedAntiWhite: function (resolve, reject) {
    exec(resolve, reject, "Printer", "isForcedAntiWhite", []);
  },
  isForcedBold: function (resolve, reject) {
    exec(resolve, reject, "Printer", "isForcedBold", []);
  },
  isForcedUnderline: function (resolve, reject) {
    exec(resolve, reject, "Printer", "isForcedUnderline", []);
  },
  getForcedRowHeight: function (resolve, reject) {
    exec(resolve, reject, "Printer", "getForcedRowHeight", []);
  },
  getFontName: function (resolve, reject) {
    exec(resolve, reject, "Printer", "getFontName", []);
  },
  getPrinterDensity: function (resolve, reject) {
    exec(resolve, reject, "Printer", "getPrinterDensity", []);
  },
  print2DCode: function (data, symbology, moduleSize, errorLevel, resolve, reject) {
    exec(resolve, reject, "Printer", "print2DCode", [data, symbology, moduleSize, errorLevel]);
  },
  getPrinterPaper: function (resolve, reject) {
    exec(resolve, reject, "Printer", "getPrinterPaper", []);
  },
  autoOutPaper: function (resolve, reject) {
    exec(resolve, reject, "Printer", "autoOutPaper", []);
  },
  setPrinterStyle: function (key, value, resolve, reject) {
    exec(resolve, reject, "Printer", "setPrinterStyle", []);
  },
  printerStatusStartListener: function (onSuccess, onError) {
    exec(onSuccess, onError, "Printer", "printerStatusStartListener", []);
  },
  printerStatusStopListener: function () {
    exec(function () {}, function () {}, "Printer", "printerStatusStopListener", []);
  }
}
