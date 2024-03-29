package GerenciamentoDeConsignacao;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class AdaptadorInterfaceWeb implements Adaptador {

	public static WebDriver driver;
	public static ConhecimentoDeDominioDeInterfaceWeb conhecimento;
	
	public AdaptadorInterfaceWeb() {
		System.setProperty("webdriver.chrome.driver", "chromedriver");
		driver = new ChromeDriver();
		conhecimento = new ConhecimentoDeDominioDeInterfaceWeb();
		
		try {
			driver.get(conhecimento.get("url.inicio"));
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}
	}
	
	@Override
	public void executarEventoCriar(ContextoGerenciamentoDeConsignacao contexto) {
		// TODO Auto-generated method stub
		try {
			driver.findElement(By.cssSelector(conhecimento.get("seletor.linkCriar"))).click();
			Thread.sleep(3000);
						
			contexto.estado = Estado.CriandoConsignacao;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}
	}

	@Override
	public void executarEventoEditar(ContextoGerenciamentoDeConsignacao contexto) {
		// TODO Auto-generated method stub
		try {
			driver.findElement(By.cssSelector(conhecimento.get("seletor.linkEditar"))).click();
			Thread.sleep(3000);
						
			contexto.estado = Estado.EditandoConsignacao;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}
	}

	@Override
	public void executarEventoCriarErro(ContextoGerenciamentoDeConsignacao contexto) {
		this.executarEventoSalvarDadosErro(contexto, Estado.CriandoConsignacao, "mensagem.criarErro");
	}

	@Override
	public void executarEventoCriarSucesso(ContextoGerenciamentoDeConsignacao contexto) {
		this.executarEventoSalvarDadosSucesso(contexto, "mensagem.criarSucesso");
	}

	@Override
	public void executarEventoEditarErro(ContextoGerenciamentoDeConsignacao contexto) {
		this.executarEventoSalvarDadosErro(contexto, Estado.EditandoConsignacao, "mensagem.editarErro");
	}

	@Override
	public void executarEventoEditarSucesso(ContextoGerenciamentoDeConsignacao contexto) {
		this.executarEventoSalvarDadosSucesso(contexto, "mensagem.editarSucesso");
	}
	
	@Override
	public void executarEventoCancelar(ContextoGerenciamentoDeConsignacao contexto) {
		// TODO Auto-generated method stub
		try {
			driver.findElement(By.cssSelector(conhecimento.get("seletor.linkCancelar"))).click();
			Thread.sleep(3000);
					
			contexto.estado = Estado.ListandoConsignacoes;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}
	}
	
	private void executarEventoSalvarDadosErro(ContextoGerenciamentoDeConsignacao contexto, Estado estado, String seletorMensagem) {
		try {
			// Cliente, estoque, data, itens(quantidade de itens
			
			Assert.assertFalse("Os dados s�o v�lidos?", contexto.dadosValidos.booleanValue());
			
			new Select(driver.findElement(By.cssSelector(conhecimento.get("seletor.campoEstoque"))))
			.selectByVisibleText(conhecimento.get("dado.valido.estoque"));
			
			new Select(driver.findElement(By.cssSelector(conhecimento.get("seletor.campoItem"))))
			.selectByVisibleText(conhecimento.get("dado.valido.item"));
			
			WebElement data = driver.findElement(By.cssSelector(conhecimento.get("seletor.campoData")));
			data.clear();
			data.sendKeys(conhecimento.get("dado.invalido.data"));			
			
			WebElement quantidadeItem = driver.findElement(By.cssSelector(conhecimento.get("seletor.campoQuantidadeItem")));
			quantidadeItem.clear();
			quantidadeItem.sendKeys(conhecimento.get("dado.valido.quantidadeItem"));
			
			driver.findElement(By.cssSelector(conhecimento.get("seletor.enviarDados"))).click();
			Thread.sleep(3000);
						
			Assert.assertFalse("A mensagem de erro foi exibida? ", !driver.getPageSource().contains(conhecimento.get(seletorMensagem)));
			
			contexto.estado = estado;
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	private void executarEventoSalvarDadosSucesso(ContextoGerenciamentoDeConsignacao contexto, String seletorMensagem) {
		try {
			Assert.assertTrue("Os dados s�o v�lidos?", contexto.dadosValidos.booleanValue());
			
			new Select(driver.findElement(By.cssSelector(conhecimento.get("seletor.campoCliente"))))
			.selectByVisibleText(conhecimento.get("dado.valido.cliente"));
		
			new Select(driver.findElement(By.cssSelector(conhecimento.get("seletor.campoEstoque"))))
			.selectByVisibleText(conhecimento.get("dado.valido.estoque"));
			
			new Select(driver.findElement(By.cssSelector(conhecimento.get("seletor.campoItem"))))
			.selectByVisibleText(conhecimento.get("dado.valido.item"));
						
			WebElement data = driver.findElement(By.cssSelector(conhecimento.get("seletor.campoData")));
			data.clear();
			data.sendKeys(conhecimento.get("dado.valido.data"));			
			
			WebElement quantidadeItem = driver.findElement(By.cssSelector(conhecimento.get("seletor.campoQuantidadeItem")));
			quantidadeItem.clear();
			quantidadeItem.sendKeys(conhecimento.get("dado.valido.quantidadeItem"));
			
			driver.findElement(By.cssSelector(conhecimento.get("seletor.inserir"))).click();
			
			driver.findElement(By.cssSelector(conhecimento.get("seletor.enviarDados"))).click();
			Thread.sleep(3000);
						
			Assert.assertFalse("A mensagem de sucesso foi exibida? ", !driver.getPageSource().contains(conhecimento.get(seletorMensagem)));
			
			contexto.estado = Estado.ListandoConsignacoes;
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Override
	public void executarEventoDarBaixa(ContextoGerenciamentoDeConsignacao contexto) {
		// TODO Auto-generated method stub
		try {
			List<WebElement> itens = driver.findElements(By.cssSelector(conhecimento.get("seletor.itensBaixa")));
			
			for(int i=0; i<itens.size(); i++) {
				itens.get(i).sendKeys(conhecimento.get("dados.quantidade"));
			}
				
			
			driver.findElement(By.cssSelector(conhecimento.get("seletor.linkBaixa"))).click();
			Thread.sleep(3000);
					
			contexto.estado = Estado.DandoBaixaEmConsignacao;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}
	}

	@Override
	public void executarEventoDarBaixaSucesso(ContextoGerenciamentoDeConsignacao contexto) {
		// TODO Auto-generated method stub
		try {
			driver.findElement(By.cssSelector(conhecimento.get("seletor.salvarDados"))).click();
			Thread.sleep(3000);
					
			contexto.estado = Estado.ListandoConsignacoes;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}
	}
}
