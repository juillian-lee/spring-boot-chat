package br.com.livetouch.chat;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import java.io.IOException;
import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReaderOrdemServico {


    public static class Cliente {
        private String nome;
        private String endereco;
        private String bairro;
        private String cidade;
        private String UF;
        private String telefone;
        private String placa;
        private String ordemServico;
        private String[] servicos;

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getEndereco() {
            return endereco;
        }

        public void setEndereco(String endereco) {
            this.endereco = endereco;
        }

        public String getBairro() {
            return bairro;
        }

        public void setBairro(String bairro) {
            this.bairro = bairro;
        }

        public String getCidade() {
            return cidade;
        }

        public void setCidade(String cidade) {
            this.cidade = cidade;
        }

        public String getUF() {
            return UF;
        }

        public void setUF(String UF) {
            this.UF = UF;
        }

        public String getTelefone() {
            return telefone;
        }

        public void setTelefone(String telefone) {
            this.telefone = telefone;
        }

        public void setOrdemServico(String ordemServico) {
            this.ordemServico = ordemServico;
        }

        public String getOrdemServico() {
            return ordemServico;
        }

        public void setPlaca(String placa) {
            this.placa = placa;
        }

        public String getPlaca() {
            return placa;
        }

        public void setServicos(String[] servicos) {
            this.servicos = servicos;
        }

        public String[] getServicos() {
            return servicos;
        }

        @Override
        public String toString() {
            return nome + " \\" + endereco + " \\" + bairro + " \\" + cidade + " \\" + UF + " \\" + telefone + " \\" + ordemServico + " \\" + placa + " \\" + servicos;
        }
    }

    public static void main(String argv[]) throws Exception {
//        ReaderOrdemServico reader = new ReaderOrdemServico("/home/juillianlee/Documentos/Relatorio_SS_22TWVC_10.pdf");
        ReaderOrdemServico reader = new ReaderOrdemServico("/home/juillianlee/Documentos/Relatorio_SS_22TMWC_41.pdf");
        Cliente c = new Cliente();
        c.setBairro(reader.getBairro());
        c.setCidade(reader.getCidade());
        c.setEndereco(reader.getEndereco());
        c.setNome(reader.getNomeFantasia());
        c.setOrdemServico(reader.getOrdemServico());
        c.setPlaca(reader.getPlaca());
        c.setServicos(reader.getServicos());
        c.setTelefone(reader.getTelefone());
        c.setUF(reader.getUF());

        System.out.println(c);

    }

    private String resultantText;

    public ReaderOrdemServico(String fileName) throws IOException {
        PdfReader reader = new PdfReader(fileName);
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        TextExtractionStrategy strategy = parser.processContent(1, new SimpleTextExtractionStrategy());
        String resultantText = strategy.getResultantText();
        resultantText = Normalizer.normalize(resultantText, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toUpperCase();
        this.resultantText = resultantText;
    }

    public String getOrdemServico() {
        Pattern pattern = Pattern.compile("[0-9]+[a-zA-Z]+[\\/][0-9]+");
        Matcher matcher = pattern.matcher(resultantText);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public String getPlaca() {
        Pattern pattern = Pattern.compile("[A-Z]{3}\\d{4}");
        Matcher matcher = pattern.matcher(resultantText);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public String getNomeFantasia() {
        String search = "Nome Fantasia:".toUpperCase();
        return findAfter(search);
    }

    public String getEndereco() {
        String search = "Endereco:".toUpperCase();
        return findAfter(search);
    }

    public String getBairro() {
        String search = "Bairro:".toUpperCase();
        return findAfter(search);
    }

    public String getCidade() {
        String search = "Cidade:".toUpperCase();
        return findAfter(search);
    }

    public String getUF() {
        String search = "UF:".toUpperCase();
        return findAfter(search);
    }

    public String getContato() {
        String search = "Contato:".toUpperCase();
        return findAfter(search);
    }

    public String getTelefone() {
        String search = "fone:".toUpperCase();
        return findAfter(search);
    }

    public String[] getServicos() {
        String[] searchServicos = {
                "SERVICOS SOLICITADOS",
                "SERVICOS INDICADOS"
        };

        String servicos = null;
        for (String search : searchServicos) {
            int start = resultantText.indexOf(search);
            if (start != -1) {
                start += search.length();
                int end = resultantText.indexOf("OBSERVACOES DO SERVICO") - 1;
                servicos = resultantText.substring(start, end);
                break;
            }
        }
        if (servicos != null) {
            servicos = servicos.replaceAll("[^a-zA-Z0-9-\n]", " ");
            return servicos.split("\n");
        }
        return null;
    }

    private String findAfter(String search) {
        search = search.toUpperCase();
        int start = resultantText.indexOf(search);
        if (start != -1) {
            String partial = resultantText.substring(start + search.length());
            int end = partial.indexOf("\n");

            return end == -1 ? null : partial.substring(0, end);
        }
        return null;
    }
}
