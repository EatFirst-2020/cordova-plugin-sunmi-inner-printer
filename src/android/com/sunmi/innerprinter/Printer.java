package com.sunmi.innerprinter;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;

import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.ITax;
import woyou.aidlservice.jiuiv5.IWoyouService;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ComponentName;
import android.content.ServiceConnection;

import android.graphics.Bitmap;

import android.os.IBinder;

import android.util.Base64;
import android.util.Log;

import com.sunmi.utils.BitmapUtils;
import com.sunmi.utils.ThreadPoolManager;

public class Printer extends CordovaPlugin {
  private static final String TAG = "SunmiInnerPrinter";

  private BitmapUtils bitMapUtils;
  private IWoyouService woyouService;
  private PrinterStatusReceiver printerStatusReceiver = new PrinterStatusReceiver();

  private ServiceConnection connService = new ServiceConnection() {
    @Override
    public void onServiceDisconnected(ComponentName name) {
      woyouService = null;
      Log.d(TAG, "Service disconnected");
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      woyouService = IWoyouService.Stub.asInterface(service);
      Log.d(TAG, "Service connected");
    }
  };

  public final static String OUT_OF_PAPER_ACTION = "woyou.aidlservice.jiuv5.OUT_OF_PAPER_ACTION";
  public final static String ERROR_ACTION = "woyou.aidlservice.jiuv5.ERROR_ACTION";
  public final static String NORMAL_ACTION = "woyou.aidlservice.jiuv5.NORMAL_ACTION";
  public final static String COVER_OPEN_ACTION = "woyou.aidlservice.jiuv5.COVER_OPEN_ACTION";
  public final static String COVER_ERROR_ACTION = "woyou.aidlservice.jiuv5.COVER_ERROR_ACTION";
  public final static String KNIFE_ERROR_1_ACTION = "woyou.aidlservice.jiuv5.KNIFE_ERROR_ACTION_1";
  public final static String KNIFE_ERROR_2_ACTION = "woyou.aidlservice.jiuv5.KNIFE_ERROR_ACTION_2";
  public final static String OVER_HEATING_ACITON = "woyou.aidlservice.jiuv5.OVER_HEATING_ACITON";
  public final static String FIRMWARE_UPDATING_ACITON = "woyou.aidlservice.jiuv5.FIRMWARE_UPDATING_ACITON";

  @Override
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);

    Context applicationContext = this.cordova.getActivity().getApplicationContext();

    bitMapUtils = new BitmapUtils(applicationContext);

    Intent intent = new Intent();
    intent.setPackage("woyou.aidlservice.jiuiv5");
    intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");

    applicationContext.startService(intent);
    applicationContext.bindService(intent, connService, Context.BIND_AUTO_CREATE);

    IntentFilter mFilter = new IntentFilter();
    mFilter.addAction(OUT_OF_PAPER_ACTION);
    mFilter.addAction(ERROR_ACTION);
    mFilter.addAction(NORMAL_ACTION);
    mFilter.addAction(COVER_OPEN_ACTION);
    mFilter.addAction(COVER_ERROR_ACTION);
    mFilter.addAction(KNIFE_ERROR_1_ACTION);
    mFilter.addAction(KNIFE_ERROR_2_ACTION);
    mFilter.addAction(OVER_HEATING_ACITON);
    mFilter.addAction(FIRMWARE_UPDATING_ACITON);

    applicationContext.registerReceiver(printerStatusReceiver, mFilter);
  }

  @Override
  public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
    if (action.equals("hasPrinter")) {
      hasPrinter(callbackContext);
      return true;
    } else if (action.equals("getServiceVersion")) {
      printerInit(callbackContext);
      return true;
    } else if (action.equals("printerInit")) {
      printerInit(callbackContext);
      return true;
    } else if (action.equals("printerSelfChecking")) {
      printerSelfChecking(callbackContext);
      return true;
    } else if (action.equals("getPrinterSerialNo")) {
      getPrinterSerialNo(callbackContext);
      return true;
    } else if (action.equals("getPrinterVersion")) {
      getPrinterVersion(callbackContext);
      return true;
    } else if (action.equals("getPrinterModal")) {
      getPrinterModal(callbackContext);
      return true;
    } else if (action.equals("getPrintedLength")) {
      getPrintedLength(callbackContext);
      return true;
    } else if (action.equals("lineWrap")) {
      lineWrap(data.getInt(0), callbackContext);
      return true;
    } else if (action.equals("sendRAWData")) {
      sendRAWData(data.getString(0), callbackContext);
      return true;
    } else if (action.equals("setAlignment")) {
      setAlignment(data.getInt(0), callbackContext);
      return true;
    } else if (action.equals("setFontName")) {
      setFontName(data.getString(0), callbackContext);
      return true;
    } else if (action.equals("setFontSize")) {
      setFontSize((float) data.getDouble(0), callbackContext);
      return true;
    } else if (action.equals("printText")) {
      printText(data.getString(0), callbackContext);
      return true;
    } else if (action.equals("printTextWithFont")) {
      printTextWithFont(data.getString(0), data.getString(1), (float) data.getDouble(2), callbackContext);
      return true;
    } else if (action.equals("printColumnsText")) {
      printColumnsText(data.getJSONArray(0), data.getJSONArray(1), data.getJSONArray(2), callbackContext);
      return true;
    } else if (action.equals("printBitmap")) {
      printBitmap(data.getString(0), data.getInt(1), data.getInt(2), callbackContext);
      return true;
    } else if (action.equals("printBarCode")) {
      printBarCode(data.getString(0), data.getInt(1), data.getInt(2), data.getInt(3), data.getInt(4), callbackContext);
      return true;
    } else if (action.equals("printQRCode")) {
      printQRCode(data.getString(0), data.getInt(1), data.getInt(2), callbackContext);
      return true;
    } else if (action.equals("printOriginalText")) {
      printOriginalText(data.getString(0), callbackContext);
      return true;
//    } else if (action.equals("commitPrint")) {
//      commitPrint(data.getString(0), callbackContext);
//      return true;
    } else if (action.equals("commitPrinterBuffer")) {
      commitPrinterBuffer(callbackContext);
      return true;
    } else if (action.equals("enterPrinterBuffer")) {
      enterPrinterBuffer(data.getBoolean(0), callbackContext);
      return true;
    } else if (action.equals("tax")) {
      tax(data.getString(0), callbackContext);
      return true;
    } else if (action.equals("getPrinterFactory")) {
      getPrinterFactory(callbackContext);
      return true;
    } else if (action.equals("clearBuffer")) {
      clearBuffer(callbackContext);
      return true;
    } else if (action.equals("commitPrinterBufferWithCallback")) {
      commitPrinterBufferWithCallback(callbackContext);
      return true;
    } else if (action.equals("exitPrinterBufferWithCallback")) {
      exitPrinterBufferWithCallback(data.getBoolean(0), callbackContext);
      return true;
    } else if (action.equals("printColumnsString")) {
      printColumnsString(data.getJSONArray(0), data.getJSONArray(1), data.getJSONArray(2), callbackContext);
      return true;
    } else if (action.equals("updatePrinterState")) {
      updatePrinterState(callbackContext);
      return true;
    } else if (action.equals("printBitmapCustom")) {
      printBitmapCustom(data.getString(0), data.getInt(1), data.getInt(2), data.getInt(3), callbackContext);
      return true;
    } else if (action.equals("getForcedDouble")) {
      getForcedDouble(callbackContext);
      return true;
    } else if (action.equals("isForcedAntiWhite")) {
      isForcedAntiWhite(callbackContext);
      return true;
    } else if (action.equals("isForcedBold")) {
      isForcedBold(callbackContext);
      return true;
    } else if (action.equals("isForcedUnderline")) {
      isForcedUnderline(callbackContext);
      return true;
    } else if (action.equals("getForcedRowHeight")) {
      getForcedRowHeight(callbackContext);
      return true;
    } else if (action.equals("getFontName")) {
      getFontName(callbackContext);
      return true;
    } else if (action.equals("getPrinterDensity")) {
      getPrinterDensity(callbackContext);
      return true;
    } else if (action.equals("print2DCode")) {
      print2DCode(data.getString(0), data.getInt(1), data.getInt(2), data.getInt(3), callbackContext);
      return true;
    } else if (action.equals("getPrinterPaper")) {
      getPrinterPaper(callbackContext);
      return true;
    } else if (action.equals("autoOutPaper")) {
      autoOutPaper(callbackContext);
      return true;
    } else if (action.equals("setPrinterStyle")) {
      setPrinterStyle(data.getInt(0), data.getInt(1), callbackContext);
      return true;
    } else if (action.equals("cutPaper")) {
      cutPaper(callbackContext);
      return true;
    } else if (action.equals("getCutPaperTimes")) {
      getCutPaperTimes(callbackContext);
      return true;
    } else if (action.equals("openDrawer")) {
      openDrawer(callbackContext);
      return true;
    } else if (action.equals("getOpenDrawerTimes")) {
      getOpenDrawerTimes(callbackContext);
      return true;
    } else if (action.equals("getDrawerStatus")) {
      getDrawerStatus(callbackContext);
      return true;
    } else if (action.equals("printerStatusStartListener")) {
      printerStatusStartListener(callbackContext);
      return true;
    } else if (action.equals("printerStatusStopListener")) {
      printerStatusStopListener();
      return true;
    }

    return false;
  }

  private ICallback.Stub newCallbackStub(final CallbackContext callbackContext) {
    return new ICallback.Stub() {
      @Override
      public void onRunResult(boolean isSuccess) {
        if (isSuccess) {
          callbackContext.success("");
        } else {
          callbackContext.error(isSuccess + "");
        }
      }

      @Override
      public void onPrintResult(int code, String msg) {
        // TODO: 事务打印的时候，commitPrinterBuffer/exitPrinterBuffer的回调在这里处理
      }

      @Override
      public void onReturnString(String result) {
        callbackContext.success(result);
      }

      @Override
      public void onRaiseException(int code, String msg) {
        callbackContext.error(msg);
      }
    };
  }

  public void getServiceVersion(final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    try {
      callbackContext.success(printerService.getServiceVersion());
    } catch (Exception e) {
      callbackContext.error(e.getMessage());
    }
  }

  public void printerInit(final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          printerService.printerInit(newCallbackStub(callbackContext));
        } catch (Exception e) {
          callbackContext.error(e.getMessage());
        }
      }
    });
  }

  public void printerSelfChecking(final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          printerService.printerSelfChecking(newCallbackStub(callbackContext));
        } catch (Exception e) {
          callbackContext.error(e.getMessage());
        }
      }
    });
  }

  public void getPrinterSerialNo(final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    try {
      callbackContext.success(printerService.getPrinterSerialNo());
    } catch (Exception e) {
      callbackContext.error(e.getMessage());
    }
  }

  public void getPrinterVersion(final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    try {
      callbackContext.success(printerService.getPrinterVersion());
    } catch (Exception e) {
      callbackContext.error(e.getMessage());
    }
  }

  public void getPrinterModal(final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    try {
      callbackContext.success(printerService.getPrinterModal());
    } catch (Exception e) {
      Log.i(TAG, "ERROR: " + e.getMessage());
      callbackContext.error(e.getMessage());
    }
  }

  public void hasPrinter(final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    callbackContext.success(printerService == null ? 0 : 1);
  }

  public void getPrintedLength(final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          printerService.getPrintedLength(newCallbackStub(callbackContext));
        } catch (Exception e) {
          callbackContext.error(e.getMessage());
        }
      }
    });
  }

  public void lineWrap(int n, final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    final int count = n;
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          printerService.lineWrap(count, newCallbackStub(callbackContext));
        } catch (Exception e) {
          callbackContext.error(e.getMessage());
        }
      }
    });
  }

  public void sendRAWData(String base64EncriptedData, final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    final byte[] d = Base64.decode(base64EncriptedData, Base64.DEFAULT);
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          printerService.sendRAWData(d, newCallbackStub(callbackContext));
        } catch (Exception e) {
          callbackContext.error(e.getMessage());
        }
      }
    });
  }

  public void setAlignment(int alignment, final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    final int align = alignment;
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          printerService.setAlignment(align, newCallbackStub(callbackContext));
        } catch (Exception e) {
          callbackContext.error(e.getMessage());
        }
      }
    });
  }

  public void setFontName(String typeface, final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    final String tf = typeface;
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          printerService.setFontName(tf, newCallbackStub(callbackContext));
        } catch (Exception e) {
          callbackContext.error(e.getMessage());
        }
      }
    });
  }

  public void setFontSize(float fontsize, final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    final float fs = fontsize;
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          printerService.setFontSize(fs, newCallbackStub(callbackContext));
        } catch (Exception e) {
          callbackContext.error(e.getMessage());
        }
      }
    });
  }

  public void printText(String text, final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    final String txt = text;
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          printerService.printText(txt, newCallbackStub(callbackContext));
        } catch (Exception e) {
          callbackContext.error(e.getMessage());
        }
      }
    });
  }

  public void printTextWithFont(String text, String typeface, float fontsize, final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    final String txt = text;
    final String tf = typeface;
    final float fs = fontsize;
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          printerService.printTextWithFont(txt, tf, fs, newCallbackStub(callbackContext));
        } catch (Exception e) {
          callbackContext.error(e.getMessage());
        }
      }
    });
  }

  public void printColumnsText(JSONArray colsTextArr, JSONArray colsWidthArr, JSONArray colsAlign,
                               final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    final String[] clst = new String[colsTextArr.length()];
    for (int i = 0; i < colsTextArr.length(); i++) {
      try {
        clst[i] = colsTextArr.getString(i);
      } catch (JSONException e) {
        clst[i] = "-";
        Log.i(TAG, "ERROR TEXT: " + e.getMessage());
      }
    }
    final int[] clsw = new int[colsWidthArr.length()];
    for (int i = 0; i < colsWidthArr.length(); i++) {
      try {
        clsw[i] = colsWidthArr.getInt(i);
      } catch (JSONException e) {
        clsw[i] = 1;
        Log.i(TAG, "ERROR WIDTH: " + e.getMessage());
      }
    }
    final int[] clsa = new int[colsAlign.length()];
    for (int i = 0; i < colsAlign.length(); i++) {
      try {
        clsa[i] = colsAlign.getInt(i);
      } catch (JSONException e) {
        clsa[i] = 0;
        Log.i(TAG, "ERROR ALIGN: " + e.getMessage());
      }
    }
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          printerService.printColumnsText(clst, clsw, clsa, newCallbackStub(callbackContext));
        } catch (Exception e) {
          callbackContext.error(e.getMessage());
        }
      }
    });
  }

  public void printBitmap(String data, int width, int height, final CallbackContext callbackContext) {
    try {
      final IWoyouService printerService = woyouService;
      byte[] decoded = Base64.decode(data, Base64.DEFAULT);
      final Bitmap bitMap = bitMapUtils.decodeBitmap(decoded, width, height);
      ThreadPoolManager.getInstance().executeTask(new Runnable() {
        @Override
        public void run() {
          try {
            printerService.printBitmap(bitMap, newCallbackStub(callbackContext));
          } catch (Exception e) {
            callbackContext.error(e.getMessage());
          }
        }
      });
    } catch (Exception e) {
      callbackContext.error(e.getMessage());
    }
  }

  public void printBarCode(String data, int symbology, int height, int width, int textPosition,
                           final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    final String d = data;
    final int s = symbology;
    final int h = height;
    final int w = width;
    final int tp = textPosition;

    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          printerService.printBarCode(d, s, h, w, tp, newCallbackStub(callbackContext));
        } catch (Exception e) {
          callbackContext.error(e.getMessage());
        }
      }
    });
  }

  public void printQRCode(String data, int moduleSize, int errorLevel, final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    final String d = data;
    final int size = moduleSize;
    final int level = errorLevel;
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          printerService.printQRCode(d, size, level, newCallbackStub(callbackContext));
        } catch (Exception e) {
          callbackContext.error(e.getMessage());
        }
      }
    });
  }

  public void printOriginalText(String text, final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    final String txt = text;
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          printerService.printOriginalText(txt, newCallbackStub(callbackContext));
        } catch (Exception e) {
          callbackContext.error(e.getMessage());
        }
      }
    });
  }

//  public void commitPrint(TransBean[] transbean, final CallbackContext callbackContext) {
//    // TODO
//  }

  public void commitPrinterBuffer(final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    try {
      printerService.commitPrinterBuffer();
      callbackContext.success();
    } catch (Exception e) {
      callbackContext.error(e.getMessage());
    }
  }

  public void enterPrinterBuffer(boolean clean, final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    final boolean c = clean;
    try {
      printerService.enterPrinterBuffer(c);
      callbackContext.success();
    } catch (Exception e) {
      callbackContext.error(e.getMessage());
    }
  }

  public void exitPrinterBuffer(boolean commit, final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    final boolean c = commit;
    try {
      printerService.exitPrinterBuffer(c);
      callbackContext.success();
    } catch (Exception e) {
      callbackContext.error(e.getMessage());
    }
  }

  public void tax(String base64Data, final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    try {
      final byte[] d = Base64.decode(base64Data, Base64.DEFAULT);
      ThreadPoolManager.getInstance().executeTask(new Runnable() {
        @Override
        public void run() {
          try {
            printerService.tax(d, new ITax.Stub() {
              @Override
              public void onDataResult(byte[] data) {
                // TODO
              }
            });
          } catch (Exception e) {
            callbackContext.error(e.getMessage());
          }
        }
      });
    } catch (Exception e) {
      callbackContext.error(e.getMessage());
    }
  }

  public void getPrinterFactory(final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          printerService.getPrinterFactory(newCallbackStub(callbackContext));
        } catch (Exception e) {
          callbackContext.error(e.getMessage());
        }
      }
    });
  }

  public void clearBuffer(final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    try {
      printerService.clearBuffer();
      callbackContext.success();
    } catch (Exception e) {
      callbackContext.error(e.getMessage());
    }
  }

  public void commitPrinterBufferWithCallback(final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          printerService.commitPrinterBufferWithCallback(newCallbackStub(callbackContext));
        } catch (Exception e) {
          callbackContext.error(e.getMessage());
        }
      }
    });
  }

  public void exitPrinterBufferWithCallback(boolean commit, final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    final boolean c = commit;
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          printerService.exitPrinterBufferWithCallback(c, newCallbackStub(callbackContext));
        } catch (Exception e) {
          callbackContext.error(e.getMessage());
        }
      }
    });
  }

  public void printColumnsString(JSONArray colsTextArr, JSONArray colsWidthArr, JSONArray colsAlign,
                                 final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    final String[] clst = new String[colsTextArr.length()];
    for (int i = 0; i < colsTextArr.length(); i++) {
      try {
        clst[i] = colsTextArr.getString(i);
      } catch (JSONException e) {
        clst[i] = "-";
        Log.i(TAG, "ERROR TEXT: " + e.getMessage());
      }
    }
    final int[] clsw = new int[colsWidthArr.length()];
    for (int i = 0; i < colsWidthArr.length(); i++) {
      try {
        clsw[i] = colsWidthArr.getInt(i);
      } catch (JSONException e) {
        clsw[i] = 1;
        Log.i(TAG, "ERROR WIDTH: " + e.getMessage());
      }
    }
    final int[] clsa = new int[colsAlign.length()];
    for (int i = 0; i < colsAlign.length(); i++) {
      try {
        clsa[i] = colsAlign.getInt(i);
      } catch (JSONException e) {
        clsa[i] = 0;
        Log.i(TAG, "ERROR ALIGN: " + e.getMessage());
      }
    }
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          printerService.printColumnsString(clst, clsw, clsa, newCallbackStub(callbackContext));
        } catch (Exception e) {
          callbackContext.error(e.getMessage());
        }
      }
    });
  }

  public void updatePrinterState(final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    try {
      callbackContext.success(printerService.updatePrinterState());
    } catch (Exception e) {
      callbackContext.error(e.getMessage());
    }
  }

  public void printBitmapCustom(String data, int width, int height, int type,
                                final CallbackContext callbackContext) {
    try {
      final IWoyouService printerService = woyouService;
      byte[] decoded = Base64.decode(data, Base64.DEFAULT);
      final Bitmap bitMap = bitMapUtils.decodeBitmap(decoded, width, height);
      final int t = type;
      ThreadPoolManager.getInstance().executeTask(new Runnable() {
        @Override
        public void run() {
          try {
            printerService.printBitmapCustom(bitMap, t, newCallbackStub(callbackContext));
          } catch (Exception e) {
            callbackContext.error(e.getMessage());
          }
        }
      });
    } catch (Exception e) {
      callbackContext.error(e.getMessage());
    }
  }

  public void getForcedDouble(final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    try {
      callbackContext.success(printerService.getForcedDouble());
    } catch (Exception e) {
      callbackContext.error(e.getMessage());
    }
  }

  public void isForcedAntiWhite(final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    try {
      callbackContext.success(printerService.isForcedAntiWhite() ? 1 : 0);
    } catch (Exception e) {
      callbackContext.error(e.getMessage());
    }
  }

  public void isForcedBold(final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    try {
      callbackContext.success(printerService.isForcedBold() ? 1 : 0);
    } catch (Exception e) {
      callbackContext.error(e.getMessage());
    }
  }

  public void isForcedUnderline(final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    try {
      callbackContext.success(printerService.isForcedUnderline() ? 1 : 0);
    } catch (Exception e) {
      callbackContext.error(e.getMessage());
    }
  }

  public void getForcedRowHeight(final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    try {
      callbackContext.success(printerService.getForcedRowHeight());
    } catch (Exception e) {
      callbackContext.error(e.getMessage());
    }
  }

  public void getFontName(final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    try {
      callbackContext.success(printerService.getFontName());
    } catch (Exception e) {
      callbackContext.error(e.getMessage());
    }
  }

  public void getPrinterDensity(final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    try {
      callbackContext.success(printerService.getPrinterDensity());
    } catch (Exception e) {
      callbackContext.error(e.getMessage());
    }
  }

  public void print2DCode(String data, int symbology, int modulesize, int errorlevel,
                          final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    final String d = data;
    final int s = symbology;
    final int m = modulesize;
    final int e = errorlevel;

    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          printerService.print2DCode(d, s, m, e, newCallbackStub(callbackContext));
        } catch (Exception e) {
          callbackContext.error(e.getMessage());
        }
      }
    });
  }

  public void getPrinterPaper(final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    try {
      callbackContext.success(printerService.getPrinterPaper());
    } catch (Exception e) {
      callbackContext.error(e.getMessage());
    }
  }

  public void autoOutPaper(final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          printerService.autoOutPaper(newCallbackStub(callbackContext));
        } catch (Exception e) {
          callbackContext.error(e.getMessage());
        }
      }
    });
  }

  public void setPrinterStyle(int key, int value, final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    try {
      printerService.setPrinterStyle(key, value);
      callbackContext.success();
    } catch (Exception e) {
      callbackContext.error(e.getMessage());
    }
  }

  public void printerStatusStartListener(final CallbackContext callbackContext) {
    final PrinterStatusReceiver receiver = printerStatusReceiver;
    receiver.startReceiving(callbackContext);
  }

  public void printerStatusStopListener() {
    final PrinterStatusReceiver receiver = printerStatusReceiver;
    receiver.stopReceiving();
  }

  public void cutPaper(final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          printerService.cutPaper(newCallbackStub(callbackContext));
        } catch (Exception e) {
          callbackContext.error(e.getMessage());
        }
      }
    });
  }

  public void getCutPaperTimes(final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    try {
      callbackContext.success(printerService.getCutPaperTimes());
    } catch (Exception e) {
      callbackContext.error(e.getMessage());
    }
  }

  public void openDrawer(final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          printerService.openDrawer(newCallbackStub(callbackContext));
        } catch (Exception e) {
          callbackContext.error(e.getMessage());
        }
      }
    });
  }

  public void getOpenDrawerTimes(final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    try {
      callbackContext.success(printerService.getOpenDrawerTimes());
    } catch (Exception e) {
      callbackContext.error(e.getMessage());
    }
  }

  public void getDrawerStatus(final CallbackContext callbackContext) {
    final IWoyouService printerService = woyouService;
    try {
      callbackContext.success(printerService.getDrawerStatus());
    } catch (Exception e) {
      callbackContext.error(e.getMessage());
    }
  }
}
