package org.cyk.utility.file.excel;

import org.cyk.utility.function.FunctionWithPropertiesAsInput;

public interface FileExcelSheetToString extends FunctionWithPropertiesAsInput<String> {

	FileExcelSheetDataArrayReader getDataArrayReader();
	FileExcelSheetDataArrayReader getDataArrayReader(Boolean injectIfNull);
	FileExcelSheetToString setDataArrayReader(FileExcelSheetDataArrayReader dataArrayReader);
	
}
