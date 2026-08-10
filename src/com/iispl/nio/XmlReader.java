package com.iispl.nio;

import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.iispl.enums.TransactionStatus;
import com.iispl.enums.TransactionType;
import com.iispl.exception.InvalidTransactionException;
import com.iispl.exception.InvalidXmlStructureException;
import com.iispl.model.TransactionRequest;
import com.iispl.model.TransactionResult;

public class XmlReader {

	List<TransactionRequest> transactionList =new ArrayList<>();
	List<TransactionResult> failureTransactionList=new ArrayList<>();

	public List<TransactionRequest> getTransactionList()
	{
		return transactionList;
	}
	public List<TransactionResult> getFailureTransactionList()
	{
		return failureTransactionList;
	}
	public void readXml(Path xmlFile) throws Exception 
	{//trying to read the xml file using filechannel
		try (FileChannel channel = FileChannel.open(xmlFile, StandardOpenOption.READ)) 
		{  
			ByteBuffer buffer =ByteBuffer.allocate(8192);
			//A Java NIO class used to temporarily hold bytes while reading from or writing to a file/channel.
			//allocate(8192)Creates an 8 KB buffer...The file doesn't have to be loaded completely into memory.


			//"ByteBuffer allows us to read the file through the FileChannel in fixed-size chunks instead of reading the file directly as one large block. 
			//We use flip() to switch from writing bytes into the buffer to reading those bytes, and clear() to reuse the buffer for the next chunk."
			StringBuilder xmlContent = new StringBuilder();

			while (channel.read(buffer) != -1) {

				buffer.flip();
				String chunk =StandardCharsets.UTF_8.decode(buffer).toString();
				xmlContent.append(chunk);
				buffer.clear();
			}
			// Parsing the XML
			Document document =parseXml(xmlContent.toString());//returns a dom tree
			Element root =document.getDocumentElement();
			try
			{
				if (root == null) 
				{
					throw new InvalidXmlStructureException( "XML does not contain a root element");
				}
				String corporateId = root.getAttribute("corporateId");
				String fileName =xmlFile.getFileName().toString();
				try
				{
					if (!fileName.contains(corporateId)) 
					{
						throw new InvalidXmlStructureException("Corporate ID "+ corporateId + " does not match file name " + fileName);
					}
					NodeList transactionNodes = root.getElementsByTagName("transaction");
					try
					{
						if (transactionNodes.getLength() == 0)
						{
							throw new InvalidTransactionException("No transactions found");
						}
						// Used to detect duplicate transaction IDs
						Set<String> transactionIds = new HashSet<>();

						//  Process every transaction

						for (int i = 0; i < transactionNodes.getLength();i++)
						{
							Node node =transactionNodes.item(i);

							Element transactionElement = (Element) node;

							// Parse XML to TransactionRequest 
							TransactionRequest request = parseTransaction(transactionElement,root);

							// Validate TransactionRequest 
							try
							{
								validateTransaction( request,transactionIds);
								transactionList.add(request);
							}
							catch(InvalidTransactionException | InvalidXmlStructureException e)
							{
								String transactionId=request.getTransactionId();
								TransactionStatus transacrionStatus=TransactionStatus.FAILED;
								String failureCode="";
								String failureReason=e.getMessage();

								failureTransactionList.add(new TransactionResult(transactionId, transacrionStatus, failureCode, failureReason));

							}
						}
					}
					catch(InvalidTransactionException e)
					{
						System.out.println(e.getMessage());    	
					}   
				}
				catch(InvalidXmlStructureException e)
				{
					System.out.println(e.getMessage());
				}

			}
			catch(InvalidXmlStructureException e)
			{
				System.out.println(e.getMessage());
			}

		}
	}


	// PARSE XML:This method takes the XML text stored in xmlContent and converts it into a DOM Document object,
//parseXml() takes the XML content stored as a String, uses DocumentBuilder to parse it, 
	//and converts it into a DOM Document tree so that the application can easily navigate and extract XML elements.
	private Document parseXml( String xmlContent)throws Exception {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder =factory.newDocumentBuilder();
		//DocumentBuilderFactory is used to create a DocumentBuilder.
		//The DocumentBuilder will take your XML and convert it into a DOM Document.
		InputSource source = new InputSource( new StringReader(xmlContent));//XML String and provide it to the XML parser as an input source
		return builder.parse(source);
	}


	// PARSE TRANSACTION
	private TransactionRequest parseTransaction(Element transactionElement,Element root)throws Exception
	{

		String corporateId=root.getAttribute("corporateId");
		String transactionId = transactionElement.getElementsByTagName("transactionId")
				.item(0).getTextContent();
		String fromAccount =transactionElement.getElementsByTagName("fromAccount")
				.item(0).getTextContent();
		String toAccount = transactionElement.getElementsByTagName("toAccount")
				.item(0).getTextContent();
		String typeText = transactionElement.getElementsByTagName("type")
				.item(0).getTextContent();
		String amountText = transactionElement.getElementsByTagName("amount")
				.item(0).getTextContent();
		String transactionDateText = transactionElement.getElementsByTagName("transactionDate")
				.item(0).getTextContent();
		TransactionType type=TransactionType.valueOf(typeText);
		BigDecimal amount=new BigDecimal(amountText);
		LocalDate transactionDate=LocalDate.parse(transactionDateText);
		// Create model object
		TransactionRequest request = new TransactionRequest(transactionId,corporateId,fromAccount,toAccount,amount,type,transactionDate);
		return request;
	}



	// VALIDATE TRANSACTION

	private void validateTransaction(TransactionRequest request,Set<String> transactionIds) throws Exception 
	{
		//  Validate Transaction ID
		String transactionId =request.getTransactionId();
		if (transactionId == null) {

			throw new InvalidTransactionException("Transaction ID is mandatory");
		}

		if (!transactionId.startsWith("TXN")) {

			throw new InvalidTransactionException("Invalid transaction ID: " + transactionId);
		}

		// Check duplicate transaction ID

		if (!transactionIds.add(transactionId)) {

			throw new InvalidTransactionException( "Duplicate transaction ID: " + transactionId);
		}

		//  Validate From Account
		String fromAccount = request.getFromAccount();


		if (fromAccount == null) {

			throw new InvalidTransactionException( "From account is mandatory for " + transactionId);
		}

		//  Validate To Account
		String toAccount =request.getToAccount();

		if (toAccount == null) {

			throw new InvalidTransactionException( "To account is mandatory for " + transactionId);
		}

		// From and To accounts cannot be same
		if (fromAccount.equals(toAccount)) {

			throw new InvalidTransactionException("From account and To account " + "cannot be same for "+ transactionId);
		}

		//  Validate Transaction Type
		TransactionType type = request.getTransactionType();

		if (type == null) {

			throw new InvalidTransactionException(
					"Transaction type cannot be null for " + transactionId);
		}

		//  Validate Amount
		BigDecimal amount = request.getAmount();
		if (amount == null) {

			throw new InvalidTransactionException(
					"Amount cannot be null for " + transactionId);
		}

		if (amount.compareTo( BigDecimal.ZERO) <= 0) {

			throw new InvalidTransactionException(
					"Amount must be greater than zero for " + transactionId);
		}

		//  Validate Transaction Date
		LocalDate transactionDate = request.getTransactionDate();

		if (transactionDate == null) {

			throw new InvalidTransactionException(
					"Transaction date cannot be null for " + transactionId);
		}
	}
}
