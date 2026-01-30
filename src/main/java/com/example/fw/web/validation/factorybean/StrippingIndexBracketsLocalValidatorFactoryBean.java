package com.example.fw.web.validation.factorybean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * RestAPI（RestController）での入力チェックの際に使用するValidatorFactoryBeanの拡張版。
 * LocalValidatorFactoryBeanを拡張し、フィールド名からインデックス部分（[0]、[1]、…）を除去したコードも
 * エラーメッセージの解決に使用するようにしたValidatorFactoryBean<br>
 * これにより、例えば"items[0].name"のようなフィールド名に対しても、
 * messages.propertiesには"items.name"のような形でフィールド名を定義できるようになる。
 */
public class StrippingIndexBracketsLocalValidatorFactoryBean extends LocalValidatorFactoryBean {

    @Override
    protected MessageSourceResolvable getResolvableField(final String objectName, final String field) {
        // 親クラスと同じロジックを使用したコードを取得
        MessageSourceResolvable original = super.getResolvableField(objectName, field);
        String[] codes = original.getCodes();
        if (codes == null) {
            return original;
        }
        List<String> newCodesList = new ArrayList<>(Arrays.asList(codes));
        // リスト（配列）のインデックス部分の正規表現
        // 例: items[0].name, items[1].name の [0], [1] 部分だけを取り除く
        for (String code : codes) {
            // fieldが当該正規表現にマッチする場合はインデックス部分だけを除去したものも追加
            // 例: "items[0].name" -> "items.name"
            String strippedField = code.replaceAll("\\[\\d+\\]", "");
            if (!strippedField.equals(code)) {
                newCodesList.add(strippedField);
            }
        }
        // 末端のフィールド名も追加
        // 例: "items[0].name" -> "name"
        String[] fieldParts = field.split("\\.");
        String lastFieldPart = fieldParts[fieldParts.length - 1];
        if (!newCodesList.contains(lastFieldPart)) {
            newCodesList.add(lastFieldPart);
        }
        return new DefaultMessageSourceResolvable(newCodesList.toArray(new String[newCodesList.size()]), field);
    }
}
